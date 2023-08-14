package com.recruitmentsystem.service;

import com.recruitmentsystem.common.exception.ResourceAlreadyExistsException;
import com.recruitmentsystem.common.exception.ResourceNotFoundException;
import com.recruitmentsystem.common.exception.ResourceNotModifiedException;
import com.recruitmentsystem.entity.User;
import com.recruitmentsystem.mapper.UserMapper;
import com.recruitmentsystem.model.pagination.MyPagination;
import com.recruitmentsystem.model.user.UserChangePassword;
import com.recruitmentsystem.model.user.UserDisplayModel;
import com.recruitmentsystem.model.user.UserRequestModel;
import com.recruitmentsystem.repository.IUserRepository;
import com.recruitmentsystem.security.auth.AuthenticationResponse;
import com.recruitmentsystem.security.jwt.JwtTokenUtil;
import com.recruitmentsystem.security.token.Token;
import com.recruitmentsystem.security.token.TokenService;
import com.recruitmentsystem.security.token.TokenType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final IUserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtTokenUtil jwtTokenUtil;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private boolean checkDuplicateUsername(String username) {
        if (userRepository.existsUserByUsername(username)) {
            throw new ResourceAlreadyExistsException("username already taken");
        }
        return false;
    }

    private boolean checkDuplicateEmail(String email) {
        if (userRepository.existsUserByEmail(email)) {
            throw new ResourceAlreadyExistsException("email already taken");
        }
        return false;
    }

    @Transactional
    public void addUser(UserRequestModel request) {
        try {
            if (!checkDuplicateUsername(request.username()) && !checkDuplicateEmail(request.email())) {
                User user = userMapper.userRequestModelToUser(request);
                user.setCreatedAt(Instant.now());
                user.setCreatedBy(getCurrentUser().getId());
                userRepository.save(user);
                userRepository.enableUser(user.getEmail());
            }
        } catch (ResourceAlreadyExistsException e) {
            throw new ResourceAlreadyExistsException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<UserDisplayModel> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .filter(user -> !user.isDeleteFlag())
                .map(userMapper::userToDisplayModel)
                .collect(Collectors.toList());
    }

    private List<UserDisplayModel> getUsers(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

        Page<User> pagedResult = userRepository.findAll(paging);

        List<UserDisplayModel> list = pagedResult.getContent()
                .stream()
                .filter(u -> !u.isDeleteFlag())
                .map(userMapper::userToDisplayModel)
                .collect(Collectors.toList());

        if (pagedResult.hasContent()) {
            return list;
        } else {
            return new ArrayList<>();
        }
    }

    public MyPagination<UserDisplayModel> getAllUsers(Integer pageNo, Integer pageSize, String sortBy) {
        int pageElements = userRepository.countAllUser();

        List<UserDisplayModel> list = getUsers(pageNo, pageSize, sortBy);

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
                .list(Collections.singletonList(list))
                .build();
        return pagination;
    }

    public UserDisplayModel findById(Integer id) {
        return userRepository.findById(id)
                .filter(user -> !user.isDeleteFlag())
                .map(userMapper::userToDisplayModel)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " does not exist"));
    }

    private User findUserById(Integer id) {
        return userRepository.findById(id)
                .filter(user -> !user.isDeleteFlag())
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " does not exist"));
    }

    private User findUserByUsername(String username) {
        List<User> users = userRepository
                .findUserByUsername(username)
                .stream()
                .filter(user -> !user.isDeleteFlag())
                .collect(Collectors.toList());
        if (users.size() == 0) {
            throw new ResourceNotFoundException("User with username " + username + " does not exist");
        }
        return users.get(0);
    }

    private List<UserDisplayModel> findAllUserByUsername(String name) {
        return userRepository.findAll()
                .stream()
                .filter(user -> (!user.isDeleteFlag() &&
                        user.getUsername().contains(name)))
                .map(userMapper::userToDisplayModel)
                .collect(Collectors.toList());
    }

    private List<UserDisplayModel> findAllUserByFirstName(String name) {
        return userRepository.findAll()
                .stream()
                .filter(user -> (!user.isDeleteFlag() &&
                        user.getFirstName().contains(name)))
                .map(userMapper::userToDisplayModel)
                .collect(Collectors.toList());
    }

    private List<UserDisplayModel> findAllUserByLastName(String name) {
        return userRepository.findAll()
                .stream()
                .filter(user -> (!user.isDeleteFlag() &&
                        user.getLastName().contains(name)))
                .map(userMapper::userToDisplayModel)
                .collect(Collectors.toList());
    }

    public List<UserDisplayModel> findAllUserByName(String name) {
        List<UserDisplayModel> list = new ArrayList<>();
        list.addAll(findAllUserByUsername(name));
        list.addAll(findAllUserByFirstName(name));
        list.addAll(findAllUserByLastName(name));
        return list;
    }

    public User findUserByToken(String token) {
        String email = jwtTokenUtil.extractEmail(token);
        User user = null;
        try {
            user = findUserByEmail(email);
        } catch (Exception ex) {
            throw new ResourceNotFoundException("User with token " + token + " does not exist");
        }
        return user;
    }

    public UserDisplayModel findUserDisplayByToken(String token) {
        return userMapper.userToDisplayModel(findUserByToken(token));
    }

    public User findUserByEmail(String email) {
        return userRepository.findTopByEmail(email)
                .filter(user -> !user.isDeleteFlag())
                .orElseThrow(() -> new ResourceNotFoundException("User with email " + email + " does not exist"));
    }

    @Transactional
    public void updateUser(Integer id, UserRequestModel request) {
        User updateUser = findUserById(id);
        updateUser(updateUser, request, getCurrentUser().getId());
    }

    private void updateUser(User updateUser, UserRequestModel request, Integer updateBy) {
        int id = updateUser.getId();

//        if (updateUser.equals(request)){
//            throw new ResourceNotModifiedException("user information has no change");
//        }

        try {
            boolean isUsernameChange = !updateUser.getUsername().equals(request.username());
            boolean isEmailChange = !updateUser.getEmail().equals(request.email());

            boolean isValidUsername = !(isUsernameChange && checkDuplicateUsername(request.username()));
            boolean isValidEmail = !(isEmailChange && checkDuplicateEmail(request.email()));

            if (isValidUsername && isValidEmail) {
                // tao ban ghi luu thong tin cu cua user
                User oldUser = new User(updateUser, true);
                userRepository.save(oldUser);

                // update user
                updateUser = userMapper.userRequestModelToUser(request);
                updateUser.setId(id);
                updateUser.setPassword(oldUser.getPassword());
                updateUser.setRole(oldUser.getRole());
                updateUser.setCreatedAt(oldUser.getCreatedAt());
                updateUser.setCreatedBy(oldUser.getCreatedBy());
                updateUser.setUpdatedAt(Instant.now());
                updateUser.setEnabled(oldUser.getEnabled());
                updateUser.setUpdatedBy(updateBy);

                //        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
                ////        user.setPhotos(fileName);
                //        updateUser.setImgUrl(fileName);
                //        String uploadDir = "user-photos/" + id;
                //        FileService.saveFile(uploadDir, fileName, multipartFile);
                userRepository.save(updateUser);
            }
        } catch (ResourceAlreadyExistsException e) {
            throw new ResourceAlreadyExistsException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Transactional
    public AuthenticationResponse updateUser(String token, UserRequestModel request) {
        User updateUser = findUserByToken(token);
        updateUser(updateUser, request, updateUser.getId());

        String accessToken = jwtTokenUtil.generateToken(updateUser);
        String refreshToken = jwtTokenUtil.generateRefreshToken(updateUser);

        Token tokenAccess = Token
                .builder()
                .user(updateUser)
                .token(accessToken)
                .tokenType(TokenType.BEARER)
                .createdAt(Instant.now())
                .expiresAt(Instant.now().plusMillis(jwtTokenUtil.extractExpiration(accessToken).getTime()))
                .expired(false)
                .revoked(false)
                .build();

        Token tokenRefresh = Token
                .builder()
                .user(updateUser)
                .token(refreshToken)
                .tokenType(TokenType.BEARER)
                .createdAt(Instant.now())
                .expiresAt(Instant.now().plusMillis(jwtTokenUtil.extractExpiration(refreshToken).getTime()))
                .expired(false)
                .revoked(false)
                .build();

        tokenService.saveToken(tokenAccess);
        tokenService.saveToken(tokenRefresh);

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Transactional
    public AuthenticationResponse updateUserByAuth(UserRequestModel request) {
        User updateUser = getCurrentUser();
        updateUser(updateUser, request, updateUser.getId());

        String accessToken = jwtTokenUtil.generateToken(updateUser);
        String refreshToken = jwtTokenUtil.generateRefreshToken(updateUser);

        Token tokenAccess = Token
                .builder()
                .user(updateUser)
                .token(accessToken)
                .tokenType(TokenType.BEARER)
                .createdAt(Instant.now())
                .expiresAt(Instant.now().plusMillis(jwtTokenUtil.extractExpiration(accessToken).getTime()))
                .expired(false)
                .revoked(false)
                .build();

        Token tokenRefresh = Token
                .builder()
                .user(updateUser)
                .token(refreshToken)
                .tokenType(TokenType.BEARER)
                .createdAt(Instant.now())
                .expiresAt(Instant.now().plusMillis(jwtTokenUtil.extractExpiration(refreshToken).getTime()))
                .expired(false)
                .revoked(false)
                .build();

        tokenService.saveToken(tokenAccess);
        tokenService.saveToken(tokenRefresh);

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private User getCurrentUser() {
        User user = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUsername = authentication.getName();
            user = findUserByUsername(currentUsername);
        }
        return user;
    }

    public UserDisplayModel getCurrentUserDisplay() {
        return userMapper.userToDisplayModel(getCurrentUser());
    }

    public void deleteUser(Integer id) {
        User user;
        try {
            user = findUserById(id);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("User with id " + id + " does not exist");
        }
        user.setDeleteFlag(true);
        userRepository.save(user);
    }

    public void changePassword(UserChangePassword request) {

        User user = getCurrentUser();
        boolean isMatch = passwordEncoder.matches(request.currentPassword(), user.getPassword());
        // kiểm tra mật khẩu hiện tại
        if (isMatch) {
            // update user
//            UserRequestModel requestModel = UserRequestModel.builder()
//                    .username(user.getUsername())
//                    .email(user.getEmail())
//                    .password(request.getNewPassword())
//                    .firstName(user.getFirstName())
//                    .lastName(user.getLastName())
//                    .phoneNumber(user.getPhoneNumber())
//                    .address(user.getAddress())
//                    .gender(user.getGender())
//                    .birthday(user.getBirthday())
//                    .imgUrl(user.getImgUrl())
//                    .createdAt(user.getCreatedAt())
//                    .updatedAt(user.getUpdatedAt())
//                    .roleName(user.getRole().getRoleName())
//                    .build();
            user.setPassword(passwordEncoder.encode(request.newPassword()));

            // tao ban ghi luu thong tin cu cua user
            User oldUser = new User(user, true);
            userRepository.save(oldUser);

            // update user
            user.setUpdatedAt(Instant.now());
            user.setUpdatedBy(user.getId());

            userRepository.save(user);
        } else {
            throw new IllegalStateException("Your curent password is incorrect");
        }
    }
}
