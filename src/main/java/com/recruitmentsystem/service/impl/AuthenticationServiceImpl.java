package com.recruitmentsystem.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recruitmentsystem.common.exception.ResourceAlreadyExistsException;
import com.recruitmentsystem.common.exception.ResourceNotFoundException;
import com.recruitmentsystem.entity.User;
import com.recruitmentsystem.mapper.UserMapper;
import com.recruitmentsystem.model.user.UserRequestModel;
import com.recruitmentsystem.repository.IUserRepository;
import com.recruitmentsystem.security.auth.AuthenticationRequest;
import com.recruitmentsystem.security.auth.AuthenticationResponse;
import com.recruitmentsystem.security.jwt.JwtTokenUtil;
import com.recruitmentsystem.security.token.Token;
import com.recruitmentsystem.security.token.TokenService;
import com.recruitmentsystem.security.token.TokenType;
import com.recruitmentsystem.service.EmailService;
import com.recruitmentsystem.service.IAuthenticationService;
import com.recruitmentsystem.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements IAuthenticationService {
    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;
    private final UserService userService;
    private final TokenService tokenService;
    private final long EMAIL_EXPIRATION = 15 * 60 * 1000;

    @Override
    public AuthenticationResponse register(UserRequestModel request) {
        // check username
        String username = request.username();
        if (userRepository.existsUserByUsername(username)) {
            throw new ResourceAlreadyExistsException("username already taken");
        }

        // check email
        String email = request.email();
        if (userRepository.existsUserByEmail(email)) {
            throw new ResourceAlreadyExistsException("email already taken");
        }

        User user = userMapper.userRequestModelToUser(request);
        user.setCreatedAt(Instant.now());
        User savedUser = userRepository.save(user);

//        String refreshToken = jwtTokenUtil.generateRefreshToken(user);
//        Token token = Token
//                .builder()
//                .token(accessToken)
//                .createdAt(Instant.now())
//                .expiresAt(Instant.now().plus(15, ChronoUnit.MINUTES))
//                .user(user)
//                .build();
//        tokenService.saveToken(token);
        String accessToken = jwtTokenUtil.generateToken(user);
        saveUserToken(savedUser, accessToken, EMAIL_EXPIRATION);

        String link = "http://localhost:3000/register/confirm?token=" + accessToken;
        emailService.sendConfirmEmail(request.username(), request.email(), link);

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
//                .refreshToken(refreshToken)
                .build();
    }

    @Transactional
    public void confirmEmail(String token) {
        Token confirmationToken = tokenService
                .getToken(token)
                .orElseThrow(() ->
                        new ResourceNotFoundException("token not found"));

        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("email already confirmed");
        }
//        Instant expiredAt = confirmationToken.getExpiresAt();
//
//        if (expiredAt.isBefore(Instant.now())) {
//            throw new IllegalStateException("token expired");
//        }
        if (confirmationToken.isExpired()) {
            throw new IllegalStateException("token expired");
        }
        tokenService.setConfirmedAt(token);
        userRepository.enableUser(confirmationToken.getUser().getEmail());
    }

    @Override
    public AuthenticationResponse login(AuthenticationRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(), request.getPassword())
        );

        User user = (User) authentication.getPrincipal();
        System.out.println(user);
        if (!user.isEnabled()) {
            throw new IllegalStateException("Your account has not been verified!");
        } else if (user.isDeleteFlag()) {
            throw new IllegalStateException("Your account does not exist!");
        }
        String accessToken = jwtTokenUtil.generateToken(user);
        String refreshToken = jwtTokenUtil.generateRefreshToken(user);
        saveUserToken(user, accessToken, jwtTokenUtil.extractExpiration(accessToken).getTime());
        saveUserToken(user, refreshToken, jwtTokenUtil.extractExpiration(refreshToken).getTime());

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public AuthenticationResponse forgotPassword(String email) {
        User user = userService.findUserByEmail(email);
        String accessToken = jwtTokenUtil.generateToken(user);
        saveUserToken(user, accessToken, EMAIL_EXPIRATION);
//        Token token = Token
//                        .builder()
//                        .token(accessToken)
//                        .createdAt(Instant.now())
//                        .expiresAt(Instant.now().plus(15, ChronoUnit.MINUTES))
//                        .user(user)
//                        .build();
//        tokenService.saveToken(token);

        String link = "http://localhost:3000/reset_password?token=" + accessToken;
        emailService.sendResetPasswordEmail(user.getUsername(), user.getEmail(), link);

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
//                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public void updatePassword(String token, String newPassword) {
        try {
            confirmEmail(token);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (IllegalStateException e) {
            throw new IllegalStateException(e.getMessage());
        }

        User user = userService.findUserByToken(token);
        System.out.println("Find user by token: " + user);
        // tao ban ghi luu thong tin cu cua user
        User oldUser = new User(user, true);
        userRepository.save(oldUser);

        // update user
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdatedAt(Instant.now());
        user.setUpdatedBy(user.getId());

        // luu thong tin vao database
        userRepository.save(user);
    }

    private void saveUserToken(User user, String jwtToken, long expiresTime) {
        Token token = Token
                .builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .createdAt(Instant.now())
                .expiresAt(Instant.now().plusMillis(expiresTime))
                .expired(false)
                .revoked(false)
                .build();
        tokenService.saveToken(token);
    }

    private void revokeAllUserTokens(User user) {
        List<Token> validUserTokens = tokenService.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenService.saveAll(validUserTokens);
    }

    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtTokenUtil.extractEmail(refreshToken);
        if (userEmail != null) {
            User user = userService.findUserByEmail(userEmail);
            if (jwtTokenUtil.isTokenValid(refreshToken, user)) {
                var accessToken = jwtTokenUtil.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken, 24 * 60 * 60 * 60);
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }
}
