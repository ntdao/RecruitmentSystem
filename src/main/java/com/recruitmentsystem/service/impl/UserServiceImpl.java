package com.recruitmentsystem.service.impl;

import com.recruitmentsystem.common.exception.ResourceNotFoundException;
import com.recruitmentsystem.model.user.UserRequestModel;
import com.recruitmentsystem.model.user.UserDisplayModel;
import com.recruitmentsystem.entity.User;
import com.recruitmentsystem.mapper.UserMapper;
import com.recruitmentsystem.repository.IUserRepository;
import com.recruitmentsystem.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {
    private final IUserRepository userRepository;
    private final UserMapper userMapper;
//    private final JwtTokenUtil jwtTokenUtil;
//    private final AuthenticationManager authenticationManager;
//    private final EmailSender emailSender;
//    private final PasswordEncoder passwordEncoder;

//    @Override
//    public AuthenticationResponse register(UserRequestModel request) {
//        // check username
//        String username = request.username();
//        if(userRepository.existsUserByUsername(username)){
//            throw new ResourceAlreadyExistsException("username already taken");
//        }
//
//        // check email
//        String email = request.email();
//        if(userRepository.existsUserByEmail(email)){
//            throw new ResourceAlreadyExistsException("email already taken");
//        }
//
//        User user = userMapper.userRequestModelToUser(request);
//        user.setCreatedAt(LocalDateTime.now());
//        userRepository.save(user);
//        String jwtToken = jwtTokenUtil.generateToken(user);
//
//        ConfirmationToken confirmationToken = new ConfirmationToken(
//                jwtToken,
//                LocalDateTime.now(),
//                LocalDateTime.now().plusMinutes(15),
//                user
//        );
//        confirmationTokenService.saveConfirmationToken(confirmationToken);
//
////        var refreshToken = jwtService.generateRefreshToken(user);
////        saveUserToken(savedUser, jwtToken);
//
//        String link = "http://localhost:8080/api/v1/auth/register/confirm?token=" + jwtToken;
//        emailSender.sendConfirmEmail(request.email(), link);
//
//        return AuthenticationResponse.builder()
//                .accessToken(jwtToken)
////                .refreshToken(refreshToken)
//                .build();
//    }
//
//    @Transactional
//    public String confirmToken(String token) {
//        ConfirmationToken confirmationToken = confirmationTokenService
//                .getToken(token)
//                .orElseThrow(() ->
//                        new IllegalStateException("token not found"));
//
//        if (confirmationToken.getConfirmedAt() != null) {
//            throw new IllegalStateException("email already confirmed");
//        }
//
//        LocalDateTime expiredAt = confirmationToken.getExpiresAt();
//
//        if (expiredAt.isBefore(LocalDateTime.now())) {
//            throw new IllegalStateException("token expired");
//        }
//
//        confirmationTokenService.setConfirmedAt(token);
//        enableUser(confirmationToken.getUser().getEmail());
//        return "confirmed";
//    }
//
//    public int enableUser(String email) {
//        return userRepository.enableUser(email);
//    }
//    @Override
//    public AuthenticationResponse login(AuthenticationRequest request) {
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        request.getUsername(), request.getPassword())
//        );
//
//        User user = (User) authentication.getPrincipal();
//        String accessToken = jwtTokenUtil.generateToken(user);
//        return AuthenticationResponse.builder()
//                .accessToken(accessToken)
//                .build();
//    }
//
//    @Override
//    public AuthenticationResponse forgotPassword(String email) {
//        System.out.println("email: " + email);
//        User user = findUserByEmail(email);
//        String jwtToken = jwtTokenUtil.generateToken(user);
//
//        ConfirmationToken confirmationToken = new ConfirmationToken(
//                jwtToken,
//                LocalDateTime.now(),
//                LocalDateTime.now().plusMinutes(15),
//                user
//        );
//        confirmationTokenService.saveConfirmationToken(confirmationToken);
//
//        String link = "http://localhost:8080/api/v1/auth/forgot_password/confirm?token=" + jwtToken;
//        emailSender.sendResetPasswordEmail(user.getEmail(), link);
//
//        return AuthenticationResponse.builder()
//                .accessToken(jwtToken)
////                .refreshToken(refreshToken)
//                .build();
//    }
//
//    private User getUserByToken(String token) {
//        String username = jwtTokenUtil.extractUsername(token);
//        return findUserByUsername(username);
//    }
//
//    @Override
//    public void updatePassword(String token, String newPassword) {
//        User user = getUserByToken(token);
//        user.setPassword(passwordEncoder.encode(newPassword));
//        userRepository.save(user);
//    }

    @Override
    public void addUser(UserRequestModel userRegistrationRequest) {
        // check username
        String username = userRegistrationRequest.username();
        if(userRepository.existsUserByUsername(username)){
            throw new IllegalStateException("username already taken");
        }

        // check email
        String email = userRegistrationRequest.email();
        if(userRepository.existsUserByEmail(email)){
            throw new IllegalStateException("email already taken");
        }

        // add
        User user = userMapper.userRequestModelToUser(userRegistrationRequest);
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);
        userRepository.enableUser(user.getEmail());
    }

    @Override
    public List<UserDisplayModel> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .filter(user -> !user.isDeleteFlag())
                .map(userMapper::userToDisplayModel)
                .collect(Collectors.toList());
    }
    @Override
    public UserDisplayModel findById(Integer id) {
        return userRepository.findById(id)
                .filter(user -> !user.isDeleteFlag())
                .map(userMapper::userToDisplayModel)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " does not exist"));
    }

    @Override
    public User findUserById(Integer id) {
        return userRepository.findById(id)
                .filter(user -> !user.isDeleteFlag())
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " does not exist"));
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findUserByUsername(username)
                .filter(user -> !user.isDeleteFlag())
                .orElseThrow(() -> new ResourceNotFoundException("User with username " + username + " does not exist"));
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findTopByEmail(email)
                .filter(user -> !user.isDeleteFlag())
                .orElseThrow(() -> new ResourceNotFoundException("User with email " + email + " does not exist"));
    }
    @Override
    @Transactional
    public void updateUser(Integer id, UserRequestModel requestModel) {
        // tim user theo id
        User updateUser = findUserById(id);
//        updateUser.setUpdatedAt(LocalDateTime.now());

        // tao ban ghi luu thong tin cu cua user
        User oldUser = new User(updateUser, id, true);
        userRepository.save(oldUser);

        // update user
        updateUser = userMapper.userRequestModelToUser(requestModel);
        updateUser.setId(id);
        updateUser.setCreatedAt(oldUser.getCreatedAt());
        updateUser.setCreatedBy(oldUser.getCreatedBy());
        updateUser.setUpdatedAt(oldUser.getUpdatedAt());
//        updateUser.setUpdatedBy();
        userRepository.save(updateUser);
    }
    @Override
    public void deleteUser(Integer id) {
        User user = findUserById(id);
        user.setDeleteFlag(true);
        userRepository.save(user);
    }
}
