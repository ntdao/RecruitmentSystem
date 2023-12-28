package com.recruitmentsystem.service;

import com.recruitmentsystem.entity.Account;
import com.recruitmentsystem.pagination.PageDto;
import com.recruitmentsystem.repository.AccountRepository;
import com.recruitmentsystem.dto.AuthenticationResponseModel;
import com.recruitmentsystem.enums.TokenType;
import com.recruitmentsystem.exception.InputException;
import com.recruitmentsystem.exception.ResourceNotFoundException;
import com.recruitmentsystem.dto.ChangePasswordDto;
import com.recruitmentsystem.dto.UserRequestModel;
import com.recruitmentsystem.dto.UserResponseModel;
import com.recruitmentsystem.entity.User;
import com.recruitmentsystem.mapper.UserMapper;
import com.recruitmentsystem.repository.UserRepository;
import com.recruitmentsystem.security.jwt.JwtService;
import com.recruitmentsystem.security.token.Token;
import com.recruitmentsystem.security.token.TokenService;
import com.recruitmentsystem.entity.UserEducation;
import com.recruitmentsystem.dto.UserEducationDto;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final AccountRepository accountRepository;
    private final AccountService accountService;
    private final AddressService addressService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserEducationService userEducationService;
    private final FileService fileService;
    private final JwtService jwtService;
    private final RoleService roleService;
    private final S3Service s3Service;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Value("${app.image.root}")
    private String root;

    @Transactional
    public UserResponseModel addUser(UserRequestModel request) {
        accountService.checkDuplicateEmail(request.email());
        Account account = Account.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(roleService.findRoleByName(request.roleName()))
                .enabled(true)
                .build();
        accountRepository.save(account);

        addressService.saveAddress(request.address());

        User user = userMapper.userRequestModelToUser(request);
        user.setAccount(account);
        userRepository.save(user);

        // bị lỗi trường lastModified và lastModifiedBy
        return userMapper.userToResponseModel(user);
    }

    public List<UserResponseModel> findAllUsers() {
        return userRepository.findAllUser().stream()
                .map(userMapper::userToResponseModel).collect(Collectors.toList());
    }

    public Page<UserResponseModel> searchUser(PageDto pageDto) {
        Pageable paging = PageRequest.of(
                pageDto.getPageNo(),
                pageDto.getPageSize(),
                Sort.Direction.fromString(pageDto.getSortDir()),
                pageDto.getSortBy());
        Page<User> pagedResult = userRepository.findAllUser(paging);
        return pagedResult.map(userMapper::userToResponseModel);
    }

    public UserResponseModel findUserResponseModelById(Integer id) {
        return userMapper.userToResponseModel(findUserById(id));
    }

    private User findUserById(Integer id) {
        User user = userRepository.findUserById(id).orElse(null);
        System.out.println(user);
        return user;
//                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " does not exist"));
    }

    public List<UserResponseModel> findAllUserByName(String name) {
        return userRepository.findAllUserByName(name)
                .stream()
                .map(userMapper::userToResponseModel)
                .collect(Collectors.toList());
    }

    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User with email " + email + " does not exist"));
    }

    @Transactional
    public AuthenticationResponseModel updateUserByUser(UserRequestModel request, Principal connectedUser) {
        User updateUser = getCurrentUser(connectedUser);
        System.out.println("Update user: " + updateUser);
        return updateUser(updateUser, request);
    }

    private AuthenticationResponseModel updateUser(User updateUser, UserRequestModel request) {
        int userId = updateUser.getUserId();
        Account updateAccount = updateUser.getAccount();
        int accountId = updateAccount.getId();

        boolean isEmailChange = !updateUser.getAccount().getEmail().equals(request.email());
        if (isEmailChange) {
            accountService.checkDuplicateEmail(request.email());
        }

        Account oldAccount = new Account(updateAccount, true);
        System.out.println("Account - before update: " + oldAccount);
        accountRepository.save(oldAccount);

        updateAccount.setEmail(request.email());
        System.out.println("Account - after update: " + updateAccount);
        accountRepository.save(updateAccount);

        // tao ban ghi luu thong tin cu cua user
        User oldUser = new User(updateUser, true);
//        oldUser.setAccount(updateAccount);
        System.out.println("User - Old info: " + oldUser);
        userRepository.save(oldUser);

        addressService.updateAddress(updateUser.getAddress().getAddressId(), request.address());

        // update user
        updateUser = userMapper.userRequestModelToUser(request);
        updateUser.setUserId(userId);
        updateUser.setImgUrl(oldUser.getImgUrl());
        updateUser.setCvUrl(oldUser.getCvUrl());
        updateUser.setAccount(oldUser.getAccount());

        System.out.println("User - New info: " + updateUser);
        userRepository.save(updateUser);

        String accessToken = jwtService.generateToken(updateAccount);
        String refreshToken = jwtService.generateRefreshToken(updateAccount);

        accountService.revokeAllAccountTokens(accountId);

        Token tokenAccess = Token
                .builder()
                .account(updateAccount)
                .token(accessToken)
                .tokenType(TokenType.ACCESS)
                .expired(false)
                .revoked(false).build();

        Token tokenRefresh = Token
                .builder()
                .account(updateAccount)
                .token(refreshToken)
                .tokenType(TokenType.REFRESH)
                .expired(false)
                .revoked(false).build();

        tokenService.saveToken(tokenAccess);
        tokenService.saveToken(tokenRefresh);

        return AuthenticationResponseModel
                .builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken).build();
    }

    public void uploadUserProfileImageNoToken(Principal connectedUser, MultipartFile file) {
        User user = getCurrentUser(connectedUser);
        uploadProfileImage(user, file);
    }

    @Transactional
    public void uploadProfileImage(User user, MultipartFile file) {
        String fileDir = "img/user_profile/user_" + user.getUserId() + "/";

        String imgUrl = fileService.uploadImage(file, fileDir, root);

        System.out.println("User before upload image: " + user);

        User oldUser = new User(user, true);
        userRepository.save(oldUser);

        // update database with imgUrl
        user.setImgUrl(imgUrl);
        System.out.println("User after upload image: " + user);

        // save user
        userRepository.save(user);
    }

    @Transactional
    public void uploadUserProfileImage(Principal connectedUser, MultipartFile file) {
        User user = getCurrentUser(connectedUser);
        String oldImgUrl = user.getImgUrl();
        String imgUrl = s3Service.uploadFile("profile-images/%s/".formatted(user.getUserId()), file);

        System.out.println("User before upload image: " + user);

        User oldUser = new User(user, true);
        userRepository.save(oldUser);

        // update database with imgUrl
        user.setImgUrl(imgUrl);
        System.out.println("User after upload image: " + user);

        // save user
        userRepository.save(user);
        // delete old image
        s3Service.deleteFile(oldImgUrl);
    }

    public void uploadUserCV(Principal connectedUser, MultipartFile file) {
        User user = getCurrentUser(connectedUser);
        String cvUrl = s3Service.uploadFile("cv/%s/".formatted(user.getUserId()), file);

        System.out.println(cvUrl);
    }

    public byte[] getUserCV(Integer id) {
        User user = findUserById(id);
        byte[] cv = s3Service.downloadFile("cv/10/1700818252791-2.png");
        return cv;
    }

    public byte[] getUserProfileImage(Integer id) {
        User user = findUserById(id);

        if (StringUtils.isBlank(user.getImgUrl())) {
            throw new ResourceNotFoundException("User with id [%s] profile image not found".formatted(id));
        }

        byte[] profileImage = s3Service.downloadFile(user.getImgUrl());
        return profileImage;
    }

    public User getCurrentUser(Principal connectedUser) {
        return findUserByEmail(accountService.getCurrentAccount(connectedUser).getEmail());
    }

    public UserResponseModel getCurrentUserDisplay(Principal connectedUser) {
        return userMapper.userToResponseModel(getCurrentUser(connectedUser));
    }

    public void deleteUser(Integer id) {
        User user = findUserById(id);
        user.setDeleteFlag(true);

        Account account = user.getAccount();
        account.setDeleteFlag(true);

        accountRepository.save(account);
        userRepository.save(user);

        accountService.revokeAllAccountTokens(account.getId());
    }

    public void changePassword(ChangePasswordDto request,
                               Principal connectedUser) {
        User user = getCurrentUser(connectedUser);
        Account account = user.getAccount();
        int accountId = user.getAccount().getId();

//         check if the current password is correct
        if (!passwordEncoder.matches(request.currentPassword(), account.getPassword())) {
            throw new InputException("Wrong password");
        }
        // check if the two new passwords are the same
        if (!request.newPassword().equals(request.confirmationPassword())) {
            throw new InputException("Password are not the same");
        }

        Account oldAccount = new Account(account, true);
        accountRepository.save(oldAccount);
        System.out.println("Account - before change password: " + oldAccount);

        Account newAccount = Account.builder()
                .id(accountId)
                .email(oldAccount.getEmail())
                .password(passwordEncoder.encode(request.newPassword()))
                .enabled(true)
                .role(oldAccount.getRole())
                .deleteFlag(false)
                .oldId(null)
                .build();
        accountRepository.save(newAccount);
        System.out.println("Account - after change password: " + newAccount);

        System.out.println("User - before change password: " + user);
        // kiem tra xem co can thiet hay khong
        user.setLastModified(LocalDateTime.now());
        user.setLastModifiedBy(accountId);
        userRepository.save(user);
        System.out.println("User - before change password: " + user);

        accountService.revokeAllAccountTokens(accountId);
    }

    public Set<UserEducationDto> getUserEducation(Integer id) {
        return userEducationService.findByUser(id);
    }

    public void addUserEducation(UserEducationDto userEducationDto, Principal connectedUser) {
        User user = getCurrentUser(connectedUser);
        System.out.println("User before add education: " + user);
        System.out.println("List user education before add: " + user.getUserEducations());
        Set<UserEducation> list = userEducationService.addUserEducation(user.getUserEducations(), userEducationDto);
        System.out.println("List user education after add: " + list);
        user.setUserEducations(list);
        userRepository.save(user);
    }

    public void updateUserEducation(Integer id, UserEducationDto userEducationDto, Principal connectedUser) {
        User user = getCurrentUser(connectedUser);
        Set<UserEducation> list = userEducationService.updateUserEducation(id, userEducationDto, user.getUserEducations());
    }

    public void deleteUserEducation(Integer id, Principal connectedUser) {
        User user = getCurrentUser(connectedUser);
        Set<UserEducation> list = userEducationService.deleteUserEducation(id, user.getUserEducations());
    }
}
