package com.recruitmentsystem.service;

import com.recruitmentsystem.common.exception.ResourceAlreadyExistsException;
import com.recruitmentsystem.common.exception.ResourceNotFoundException;
import com.recruitmentsystem.common.myEnum.TokenType;
import com.recruitmentsystem.entity.User;
import com.recruitmentsystem.mapper.UserMapper;
import com.recruitmentsystem.model.auth.AuthenticationResponse;
import com.recruitmentsystem.model.pagination.MyPagination;
import com.recruitmentsystem.model.user.ChangePasswordRequest;
import com.recruitmentsystem.model.user.UserDisplayModel;
import com.recruitmentsystem.model.user.UserRequestModel;
import com.recruitmentsystem.repository.IUserRepository;
import com.recruitmentsystem.security.jwt.JwtService;
import com.recruitmentsystem.security.token.Token;
import com.recruitmentsystem.security.token.TokenService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
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
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final IUserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtService jwtService;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Value("${app.image.root}")
    private String root;

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
    public void addUser(UserRequestModel request, Principal connectedUser) {
        try {
            if (!checkDuplicateUsername(request.username()) && !checkDuplicateEmail(request.email())) {
                User user = userMapper.userRequestModelToUser(request);
                user.setCreatedAt(Instant.now());
                user.setCreatedBy(getCurrentUser(connectedUser).getId());
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

//    private User findUserByUsername(String username) {
//        return userRepository.findUserByUsername(username).filter(user -> !user.isDeleteFlag()).orElseThrow(() -> new ResourceNotFoundException("User with username " + username + " does not exist"));
//    }

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
            String email = jwtService.extractEmail(token);
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
    public void updateUser(Integer id, UserRequestModel request, Principal connectedUser) {
        User updateUser = findUserById(id);
        updateUser(updateUser, request, getCurrentUser(connectedUser).getId());
    }

    @Transactional
    public AuthenticationResponse updateUser(String token, UserRequestModel request) {
        User updateUser = findUserByToken(token);
        return updateUser(updateUser, request, updateUser.getId());
    }

    @Transactional
    public AuthenticationResponse updateUserByAuth(UserRequestModel request, Principal connectedUser) {
        User updateUser = getCurrentUser(connectedUser);
        System.out.println("Update user: " + updateUser);
        return updateUser(updateUser, request, updateUser.getId());
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
                System.out.println("Old info: " + oldUser);
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
                updateUser.setImgUrl(oldUser.getImgUrl());
                System.out.println(updateUser);
                System.out.println("New info: " + updateUser);
                userRepository.save(updateUser);

                String accessToken = jwtService.generateToken(updateUser);
                String refreshToken = jwtService.generateRefreshToken(updateUser);

                revokeAllUserTokens(updateUser);

                Token tokenAccess = Token
                        .builder()
                        .user(updateUser)
                        .token(accessToken)
                        .tokenType(TokenType.ACCESS)
                        .expired(false)
                        .revoked(false).build();

                Token tokenRefresh = Token
                        .builder()
                        .user(updateUser)
                        .token(refreshToken)
                        .tokenType(TokenType.REFRESH)
                        .expired(false)
                        .revoked(false).build();

                tokenService.saveToken(tokenAccess);
                tokenService.saveToken(tokenRefresh);

                response = AuthenticationResponse.builder().accessToken(accessToken).refreshToken(refreshToken).build();
            }
        } catch (ResourceAlreadyExistsException e) {
            throw new ResourceAlreadyExistsException(e.getMessage());
        } catch (IllegalStateException e) {
            throw new ResourceAlreadyExistsException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return response;
    }

    public void uploadUserProfileImage(String token, MultipartFile file) throws IOException {
        User user = findUserByToken(token);
        uploadProfileImage(user, file);
    }

    public void uploadUserProfileImageNoToken(Principal connectedUser, MultipartFile file) throws IOException {
        User user = getCurrentUser(connectedUser);
        uploadProfileImage(user, file);
    }

    private void uploadProfileImage(User user, MultipartFile file) throws IOException {
        // 1. Check if image is not empty
        if (file.isEmpty()) {
            throw new IllegalStateException("Cannot upload empty file [" + file.getSize() + "]");
        }
        // 2. If file is an image
        // 3. The user exists in database
        System.out.println("User upload image " + user);
        // 4. Grab some metadata from file if any
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
//        System.out.println(fileName);

        // Get the file extension
        String extension = FilenameUtils.getExtension(fileName);
        fileName = UUID.randomUUID() + "." + extension;
//        System.out.println(fileName);

        String fileDir = "img/user_profile/user_" + user.getId() + "/";
        String uploadDir = root + fileDir;
//        String uploadDir = "user_profile/" + user.getId();
//        String uploadDir = "src/main/resources/static/image/user_profile/" +  user.getId() + "/";
//        System.out.println(uploadDir);

        String filePath = FileService.saveFile(uploadDir, fileName, file);
        System.out.println(filePath);

        String imgUrl = fileDir + fileName;
//        return imgUrl;
        // 5. Store image (disk) and update database with imgUrl
        user.setImgUrl(imgUrl);
        User oldUser = new User(user, true);
        userRepository.save(oldUser);

        // update user
        user.setUpdatedAt(Instant.now());
        user.setUpdatedBy(user.getId());
        // save user
        userRepository.save(user);
    }

    public User getCurrentUser(Principal connectedUser) {
//        User user = null;
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (!(authentication instanceof AnonymousAuthenticationToken)) {
//            String currentUsername = authentication.getName();
//            user = findUserByUsername(currentUsername);
//        }
        return (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
    }

    public UserDisplayModel getCurrentUserDisplay(Principal connectedUser) {
        UserDisplayModel user = userMapper.userToDisplayModel(getCurrentUser(connectedUser));
        System.out.println("User info: " + user);
        return user;
    }

    public void deleteUser(Integer id, Principal connectedUser) {
        User user;
        try {
            user = findUserById(id);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("User with id " + id + " does not exist");
        }
        user.setDeleteFlag(true);
        user.setUpdatedAt(Instant.now());
        user.setUpdatedBy(getCurrentUser(connectedUser).getId());
        userRepository.save(user);
        revokeAllUserTokens(user);
    }

    public void changePassword(String token, ChangePasswordRequest request) {

//        User user = getCurrentUser();
        User user = findUserByToken(token);
        boolean isMatch = passwordEncoder.matches(request.currentPassword(), user.getPassword());
        // kiểm tra mật khẩu hiện tại
        if (isMatch) {
            user.setPassword(passwordEncoder.encode(request.newPassword()));

            // tao ban ghi luu thong tin cu cua user
            User oldUser = new User(user, true);
            userRepository.save(oldUser);

            // update user
            user.setUpdatedAt(Instant.now());
            user.setUpdatedBy(user.getId());

            userRepository.save(user);
            revokeAllUserTokens(user);

        } else {
            throw new IllegalStateException("Your current password is incorrect");
        }
    }

    public void changePassword(ChangePasswordRequest request, Principal connectedUser) {

        User user = getCurrentUser(connectedUser);

        // check if the current password is correct
        if (!passwordEncoder.matches(request.currentPassword(), user.getPassword())) {
            throw new IllegalStateException("Wrong password");
        }
        // check if the two new passwords are the same
        if (!request.newPassword().equals(request.confirmationPassword())) {
            throw new IllegalStateException("Password are not the same");
        }

        // update the password
        user.setPassword(passwordEncoder.encode(request.newPassword()));

        User oldUser = new User(user, true);
        userRepository.save(oldUser);

        // update user
        user.setUpdatedAt(Instant.now());
        user.setUpdatedBy(user.getId());

        // save the new password
        userRepository.save(user);

        revokeAllUserTokens(user);
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

//    public byte[] downloadImage(String fileName) {
//        return null;
//    }
}
