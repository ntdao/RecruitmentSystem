package com.recruitmentsystem.service.impl;

import com.recruitmentsystem.security.auth.AuthenticationRequest;
import com.recruitmentsystem.security.auth.AuthenticationResponse;
import com.recruitmentsystem.security.email.EmailSender;
import com.recruitmentsystem.common.exception.ResourceAlreadyExistsException;
import com.recruitmentsystem.security.jwt.JwtTokenUtil;
import com.recruitmentsystem.security.token.TokenService;
import com.recruitmentsystem.service.IAuthenticationService;
import com.recruitmentsystem.service.IUserService;
import com.recruitmentsystem.entity.User;
import com.recruitmentsystem.mapper.UserMapper;
import com.recruitmentsystem.model.user.UserRequestModel;
import com.recruitmentsystem.repository.IUserRepository;
import com.recruitmentsystem.security.token.ITokenRepository;
import com.recruitmentsystem.security.token.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements IAuthenticationService {
    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final EmailSender emailSender;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;
    private final IUserService userService;
    private final TokenService tokenService;

    @Override
    public AuthenticationResponse register(UserRequestModel request) {
        // check username
        String username = request.username();
        if(userRepository.existsUserByUsername(username)){
            throw new ResourceAlreadyExistsException("username already taken");
        }

        // check email
        String email = request.email();
        if(userRepository.existsUserByEmail(email)){
            throw new ResourceAlreadyExistsException("email already taken");
        }

        User user = userMapper.userRequestModelToUser(request);
        user.setCreatedAt(LocalDateTime.now());
        User savedUser = userRepository.save(user);
        String accessToken = jwtTokenUtil.generateToken(user);

        Token token = Token
                .builder()
                .token(accessToken)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();
        tokenService.saveToken(token);

//        String refreshToken = jwtTokenUtil.generateRefreshToken(user);
//        saveUserToken(savedUser, accessToken);

        String link = "http://localhost:8080/api/v1/auth/register/confirm?token=" + accessToken;
        emailSender.sendConfirmEmail(request.email(), link);

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
//                .refreshToken(refreshToken)
                .build();
    }

    @Transactional
    public String confirmToken(String token) {
        Token confirmationToken = tokenService
                .getToken(token)
                .orElseThrow(() ->
                        new IllegalStateException("token not found"));

        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("email already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("token expired");
        }

        tokenService.setConfirmedAt(token);
        userRepository.enableUser(confirmationToken.getUser().getEmail());
        return "confirmed";
    }

    @Override
    public AuthenticationResponse login(AuthenticationRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(), request.getPassword())
        );

        User user = (User) authentication.getPrincipal();
        String accessToken = jwtTokenUtil.generateToken(user);
//        String refreshToken = jwtTokenUtil.generateRefreshToken(user);
//        saveUserToken(user, accessToken);
        return AuthenticationResponse.builder()
                .accessToken(accessToken)
//                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public AuthenticationResponse forgotPassword(String email) {
        User user = userService.findUserByEmail(email);
        String accessToken = jwtTokenUtil.generateToken(user);
        String refreshToken = jwtTokenUtil.generateRefreshToken(user);
//        saveUserToken(user, accessToken);
        Token token = Token
                        .builder()
                        .token(accessToken)
                        .createdAt(LocalDateTime.now())
                        .expiresAt(LocalDateTime.now().plusMinutes(15))
                        .user(user)
                        .build();
        tokenService.saveToken(token);

        String link = "http://localhost:8080/api/v1/auth/forgot_password/confirm?token=" + accessToken;
        emailSender.sendResetPasswordEmail(user.getEmail(), link);

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
//                .refreshToken(refreshToken)
                .build();
    }

    private User getUserByToken(String token) {
        String username = jwtTokenUtil.extractUsername(token);
        return userService.findUserByUsername(username);
    }

    @Override
    public void updatePassword(String token, String newPassword) {
        User user = getUserByToken(token);

        // tao ban ghi luu thong tin cu cua user
        User oldUser = new User(user, user.getId(), true);
        userRepository.save(oldUser);

        // update user
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdatedAt(LocalDateTime.now());
//        updateUser.setUpdatedBy();

        // luu thong tin vao database
        userRepository.save(user);
    }

//    private void saveUserToken(User user, String jwtToken) {
//        Token token = Token
//                .builder()
//                .user(user)
//                .token(jwtToken)
//                .tokenType(TokenType.BEARER)
//                .expired(false)
//                .revoked(false)
//                .build();
//        tokenRepository.save(token);
//    }
//
//    private void revokeAllUserTokens(User user) {
//        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
//        if (validUserTokens.isEmpty())
//            return;
//        validUserTokens.forEach(token -> {
//            token.setExpired(true);
//            token.setRevoked(true);
//        });
//        tokenRepository.saveAll(validUserTokens);
//    }
//
//    public void refreshToken(
//            HttpServletRequest request,
//            HttpServletResponse response
//    ) throws IOException {
//        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
//        final String refreshToken;
//        final String userEmail;
//        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
//            return;
//        }
//        refreshToken = authHeader.substring(7);
//        userEmail = jwtTokenUtil.extractUsername(refreshToken);
//        if (userEmail != null) {
//            User user = userService.findUserByEmail(userEmail);
//            if (jwtTokenUtil.isTokenValid(refreshToken, user)) {
//                var accessToken = jwtTokenUtil.generateToken(user);
//                revokeAllUserTokens(user);
//                saveUserToken(user, accessToken);
//                var authResponse = AuthenticationResponse.builder()
//                        .accessToken(accessToken)
//                        .refreshToken(refreshToken)
//                        .build();
//                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
//            }
//        }
//    }
}
