package com.recruitmentsystem.user;

import com.recruitmentsystem.account.Account;
import com.recruitmentsystem.account.AccountRepository;
import com.recruitmentsystem.account.AccountService;
import com.recruitmentsystem.address.address.AddressService;
import com.recruitmentsystem.auth.AuthenticationResponseModel;
import com.recruitmentsystem.common.exception.InputException;
import com.recruitmentsystem.common.exception.ResourceNotFoundException;
import com.recruitmentsystem.common.myEnum.TokenType;
import com.recruitmentsystem.file.FileService;
import com.recruitmentsystem.pagination.MyPagination;
import com.recruitmentsystem.role.RoleService;
import com.recruitmentsystem.s3.S3Service;
import com.recruitmentsystem.security.jwt.JwtService;
import com.recruitmentsystem.token.Token;
import com.recruitmentsystem.token.TokenService;
import com.recruitmentsystem.usereducation.UserEducation;
import com.recruitmentsystem.usereducation.UserEducationDto;
import com.recruitmentsystem.usereducation.UserEducationService;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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

    private List<UserResponseModel> getUsers(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

        Page<User> pagedResult = userRepository.findAllUser(paging);

        List<UserResponseModel> list = pagedResult.getContent().stream()
                .map(userMapper::userToResponseModel).collect(Collectors.toList());

        if (pagedResult.hasContent()) {
            return list;
        } else {
            return new ArrayList<>();
        }
    }

    public MyPagination<UserResponseModel> getAllUsers(Integer pageNo, Integer pageSize, String sortBy) {
        int pageElements = userRepository.countAllUser();

        List<UserResponseModel> list = getUsers(pageNo, pageSize, sortBy);

        int totalPages;
        if (pageElements % pageSize == 0) {
            totalPages = pageElements / pageSize;
        } else {
            totalPages = pageElements / pageSize + 1;
        }

        MyPagination pagination = MyPagination.builder()
                .total(pageElements)
                .totalPage(totalPages)
                .pageSize(pageSize)
                .pageNo(pageNo)
                .list(Collections.singletonList(list)).build();
        return pagination;
    }

    public UserResponseModel findUserResponseModelById(Integer id) {
        return userMapper.userToResponseModel(findUserById(id));
    }

    private User findUserById(Integer id) {
        return userRepository.findById(id)
                .filter(user -> !user.isDeleteFlag())
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " does not exist"));
    }

    public List<UserResponseModel> findAllUserByName(String name) {
        return userRepository.findAllUserByName(name).stream()
                .map(userMapper::userToResponseModel)
                .collect(Collectors.toList());
    }

//    public User findUserByToken(String token) {
//        boolean isValid = tokenService.isValidToken(token);
//        if (isValid) {
//            String email = jwtService.extractEmail(token);
//            System.out.println(email);
//            User user = findUserByEmail(email);
//            System.out.println(user);
//            return user;
//        } else {
//            throw new ResourceNotFoundException("Token is invalid");
//        }
//    }

//    public UserResponseModel findUserDisplayByToken(String token) {
//        return userMapper.userToResponseModel(findUserByToken(token));
//    }

    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User with email " + email + " does not exist"));
    }

//    @Transactional
//    public void updateUserByAdmin(Integer id, UserRequestModel request, Principal connectedUser) {
//        User updateUser = findUserById(id);
//        updateUser(updateUser, request);
//    }

//    @Transactional
//    public AuthenticationResponseModel updateUserByToken(String token, UserRequestModel request) {
//        User updateUser = findUserByToken(token);
//        return updateUser(updateUser, request);
//    }

    private boolean isUserSame(User updateUser, UserRequestModel request) {
        return (request.email().equals(updateUser.getAccount().getEmail())
                && request.address().equals(updateUser.getAddress())
                && request.firstName().equals(updateUser.getFirstName())
                && request.lastName().equals(updateUser.getLastName())
                && request.birthday().isEqual(updateUser.getBirthday())
                && request.gender() == updateUser.getGender()
                && request.phoneNumber().equals(updateUser.getPhoneNumber()));
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

        if (isUserSame(updateUser, request)) {
            return null;
        }

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

        AuthenticationResponseModel response = AuthenticationResponseModel
                .builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken).build();
        return response;
    }

    public void uploadUserProfileImageNoToken(Principal connectedUser, MultipartFile file) {
        User user = getCurrentUser(connectedUser);
        uploadProfileImage(user, file);
    }

    //    @Transactional
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

    public void uploadUserProfileImage(Principal connectedUser, MultipartFile file) {
        User user = getCurrentUser(connectedUser);
        String imgUrl = s3Service.uploadFile("profile-images/%s/".formatted(user.getUserId()), file);

        System.out.println("User before upload image: " + user);

        User oldUser = new User(user, true);
        userRepository.save(oldUser);

        // update database with imgUrl
        user.setImgUrl(imgUrl);
        System.out.println("User after upload image: " + user);

        // save user
        userRepository.save(user);
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

    public void changePassword(ChangePasswordRequestModel request,
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
        user.setLastModified(LocalDateTime.now());
        user.setLastModifiedBy(accountId);
        userRepository.save(user);
        System.out.println("User - before change password: " + user);

//        // update the password
//        account.setPassword(passwordEncoder.encode(request.newPassword()));
//
//        User oldUser = new User(user, true);
//        userRepository.save(oldUser);
//
//        // update user
//        user.setUpdatedAt(Instant.now());
//        user.setUpdatedBy(user.getId());
//
//        // save the new password
//        userRepository.save(user);

        accountService.revokeAllAccountTokens(accountId);
    }

    private AuthenticationResponseModel authenticationResponse(Integer id) {
        String accessToken = tokenService.findAccessTokenByAccount(id);
        String refreshToken = tokenService.findRefreshTokenByAccount(id);
        return AuthenticationResponseModel
                .builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken).build();
    }

    public List<UserEducationDto> getUserEducation(Principal connectedUser) {
        User user = getCurrentUser(connectedUser);
        return userEducationService.findByUser(user.getUserId());
    }

    public void addUserEducation(UserEducationDto userEducationDto, Principal connectedUser) {
        User user = getCurrentUser(connectedUser);
        System.out.println("User before add education: " + user);
        System.out.println("List user education before add: " + user.getUserEducations());
        List<UserEducation> list = userEducationService.addUserEducation(user.getUserEducations(), userEducationDto);
        System.out.println("List user education after add: " + list);
        user.setUserEducations(list);
        userRepository.save(user);
    }
}
