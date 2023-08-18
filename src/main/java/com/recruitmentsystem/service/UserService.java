package com.recruitmentsystem.service;

import com.recruitmentsystem.common.exception.ResourceAlreadyExistsException;
import com.recruitmentsystem.common.exception.ResourceNotFoundException;
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
import org.springframework.core.io.ClassPathResource;
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
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.*;

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
//                user.setCreatedBy(getCurrentUser().getId());
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
        return users.stream().filter(user -> (!user.isDeleteFlag() && user.getRole().getRoleId() != 1)).map(userMapper::userToDisplayModel).collect(Collectors.toList());
    }

    private List<UserDisplayModel> getUsers(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

        Page<User> pagedResult = userRepository.findAll(paging);

        List<UserDisplayModel> list = pagedResult.getContent().stream().filter(u -> !u.isDeleteFlag()).map(userMapper::userToDisplayModel).collect(Collectors.toList());

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

        MyPagination pagination = MyPagination.builder().total(pageElements).totalPage(totalPages).pageSize(pageSize).pageNo(pageNo).list(Collections.singletonList(list)).build();
        return pagination;
    }

    public UserDisplayModel findById(Integer id) {
        return userRepository.findById(id).filter(user -> !user.isDeleteFlag()).map(userMapper::userToDisplayModel).orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " does not exist"));
    }

    private User findUserById(Integer id) {
        return userRepository.findById(id).filter(user -> !user.isDeleteFlag()).orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " does not exist"));
    }

    private User findUserByUsername(String username) {
        return userRepository.findUserByUsername(username).filter(user -> !user.isDeleteFlag()).orElseThrow(() -> new ResourceNotFoundException("User with username " + username + " does not exist"));
    }

    private List<UserDisplayModel> findAllUserByUsername(String name) {
        return userRepository.findAll().stream().filter(user -> (!user.isDeleteFlag() && user.getUsername().contains(name))).map(userMapper::userToDisplayModel).collect(Collectors.toList());
    }

    private List<UserDisplayModel> findAllUserByFirstName(String name) {
        return userRepository.findAll().stream().filter(user -> (!user.isDeleteFlag() && user.getFirstName().contains(name))).map(userMapper::userToDisplayModel).collect(Collectors.toList());
    }

    private List<UserDisplayModel> findAllUserByLastName(String name) {
        return userRepository.findAll().stream().filter(user -> (!user.isDeleteFlag() && user.getLastName().contains(name))).map(userMapper::userToDisplayModel).collect(Collectors.toList());
    }

    public List<UserDisplayModel> findAllUserByName(String name) {
        List<UserDisplayModel> list = new ArrayList<>();
        list.addAll(findAllUserByUsername(name));
        list.addAll(findAllUserByFirstName(name));
        list.addAll(findAllUserByLastName(name));
        return list;
    }

    public User findUserByToken(String token) {
        boolean isValid = tokenService.isValidToken(token);
        User user = null;
        if (isValid) {
            String email = jwtTokenUtil.extractEmail(token);
            System.out.println(email);
            try {
                user = findUserByEmail(email);
                System.out.println(user);
            } catch (Exception ex) {
                throw new ResourceNotFoundException("User with token " + token + " does not exist");
            }
        } else {
            throw new ResourceNotFoundException("Token is invalid");
        }
        return user;
    }

    public UserDisplayModel findUserDisplayByToken(String token) {
        return userMapper.userToDisplayModel(findUserByToken(token));
    }

    public User findUserByEmail(String email) {
        return userRepository.findTopByEmail(email).filter(user -> !user.isDeleteFlag()).orElseThrow(() -> new ResourceNotFoundException("User with email " + email + " does not exist"));
    }

    @Transactional
    public void updateUser(Integer id, UserRequestModel request) {
        User updateUser = findUserById(id);
        updateUser(updateUser, request, getCurrentUser().getId());
    }

    @Transactional
    public AuthenticationResponse updateUser(String token, UserRequestModel request) {
        User updateUser = findUserByToken(token);
        AuthenticationResponse response = updateUser(updateUser, request, updateUser.getId());
        return response;
    }

    private AuthenticationResponse updateUser(User updateUser, UserRequestModel request, Integer updateBy) {
        int id = updateUser.getId();
        AuthenticationResponse response = null;
//        if (updateUser.equals(request)){
//            throw new ResourceNotModifiedException("user information has no change");
//        }

        try {
            boolean isUsernameChange = !updateUser.getUsername().equals(request.username());
            boolean isEmailChange = !updateUser.getEmail().equals(request.email());
            boolean isValidUsername = true, isValidEmail = true;
            if (isUsernameChange) {
                isValidUsername = !checkDuplicateUsername(request.username());
            }
            if (isEmailChange) {
                isValidEmail = !checkDuplicateEmail(request.email());
            }

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
                updateUser.setImgUrl(request.imgUrl());
                userRepository.save(updateUser);

                String accessToken = jwtTokenUtil.generateToken(updateUser);
                String refreshToken = jwtTokenUtil.generateRefreshToken(updateUser);

                revokeAllUserTokens(updateUser);

                Token tokenAccess = Token.builder().user(updateUser).token(accessToken).tokenType(TokenType.BEARER).createdAt(Instant.now()).expiresAt(Instant.ofEpochMilli(jwtTokenUtil.extractExpiration(accessToken).getTime())).expired(false).revoked(false).build();

                Token tokenRefresh = Token.builder().user(updateUser).token(refreshToken).tokenType(TokenType.BEARER).createdAt(Instant.now()).expiresAt(Instant.ofEpochMilli(jwtTokenUtil.extractExpiration(refreshToken).getTime())).expired(false).revoked(false).build();

                tokenService.saveToken(tokenAccess);
                tokenService.saveToken(tokenRefresh);

                response = AuthenticationResponse.builder().accessToken(accessToken).refreshToken(refreshToken).build();
            }
        } catch (ResourceAlreadyExistsException e) {
            throw new ResourceAlreadyExistsException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return response;
    }

    public String uploadUserProfileImage(String token, MultipartFile file) throws IOException {
        // 1. Check if image is not empty
        if (file.isEmpty()) {
            throw new IllegalStateException("Cannot upload empty file [" + file.getSize() + "]");
        }
        // 2. If file is an image
        // 3. The user exists in database
        User user = findUserByToken(token);
        System.out.println(user);
        // 4. Grab some metadata from file if any
        // 5. Store image in ? and update database with imgUrl
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        System.out.println(fileName);
        String uploadDir = "user_profile/"+  user.getId();
//        String uploadDir = "src/main/resources/static/image/user_profile/" +  user.getId() + "/";
        System.out.println(uploadDir);
        return FileService.saveFile(uploadDir, fileName, file);
    }

//    @Transactional
//    public AuthenticationResponse updateUserByAuth(UserRequestModel request) {
//        User updateUser = getCurrentUser();
//        updateUser(updateUser, request, updateUser.getId());
//
//        String accessToken = jwtTokenUtil.generateToken(updateUser);
//        String refreshToken = jwtTokenUtil.generateRefreshToken(updateUser);
//
//        Token tokenAccess = Token
//                .builder()
//                .user(updateUser)
//                .token(accessToken)
//                .tokenType(TokenType.BEARER)
//                .createdAt(Instant.now())
//                .expiresAt(Instant.now().plusMillis(jwtTokenUtil.extractExpiration(accessToken).getTime()))
//                .expired(false)
//                .revoked(false)
//                .build();
//
//        Token tokenRefresh = Token
//                .builder()
//                .user(updateUser)
//                .token(refreshToken)
//                .tokenType(TokenType.BEARER)
//                .createdAt(Instant.now())
//                .expiresAt(Instant.now().plusMillis(jwtTokenUtil.extractExpiration(refreshToken).getTime()))
//                .expired(false)
//                .revoked(false)
//                .build();
//
//        tokenService.saveToken(tokenAccess);
//        tokenService.saveToken(tokenRefresh);
//
//        return AuthenticationResponse.builder()
//                .accessToken(accessToken)
//                .refreshToken(refreshToken)
//                .build();
//    }

    private User getCurrentUser() {
        User user = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUsername = authentication.getName();
            user = findUserByUsername(currentUsername);
        }
        return user;
    }

//    public UserDisplayModel getCurrentUserDisplay() {
//        return userMapper.userToDisplayModel(getCurrentUser());
//    }

    public void deleteUser(Integer id) {
        User user;
        try {
            user = findUserById(id);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("User with id " + id + " does not exist");
        }
        user.setDeleteFlag(true);
        userRepository.save(user);
        revokeAllUserTokens(user);
    }

    public void changePassword(String token, UserChangePassword request) {

//        User user = getCurrentUser();
        User user = findUserByToken(token);
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

    private void revokeAllUserTokens(User user) {
        List<Token> validUserTokens = tokenService.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty()) return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenService.saveAll(validUserTokens);
    }

    public byte[] downloadImage(String fileName) {
        return null;
    }
}
