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
import com.recruitmentsystem.security.jwt.JwtService;
import com.recruitmentsystem.security.token.Token;
import com.recruitmentsystem.security.token.TokenService;
import com.recruitmentsystem.common.myEnum.TokenType;
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
    private final JwtService jwtService;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;
    private final UserService userService;
    private final TokenService tokenService;

    @Override
    public void register(UserRequestModel request) {
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

        String token = jwtService.generateEmailToken(user);
        saveUserToken(savedUser, token, TokenType.EMAIL);

        String link = "http://localhost:3000/register/confirm?token=" + token;
        emailService.sendConfirmEmail(request.username(), request.email(), link);
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
        if (!user.isEnabled()) {
            throw new IllegalStateException("Your account has not been verified!");
        } else if (user.isDeleteFlag()) {
            throw new IllegalStateException("Your account does not exist!");
        }

        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        revokeAllUserTokens(user);

        saveUserToken(user, accessToken, TokenType.ACCESS);
        saveUserToken(user, refreshToken, TokenType.REFRESH);

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public AuthenticationResponse forgotPassword(String email) {
        User user = userService.findUserByEmail(email);
        String token = jwtService.generateEmailToken(user);
        saveUserToken(user, token, TokenType.EMAIL);

        String link = "http://localhost:3000/reset-password?token=" + token;
        emailService.sendResetPasswordEmail(user.getUsername(), user.getEmail(), link);

        return AuthenticationResponse.builder()
                .accessToken(token)
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
        revokeAllUserTokens(user);

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

    public void saveUserToken(User user, String jwtToken, TokenType type) {
        Token token = Token
                .builder()
                .user(user)
                .token(jwtToken)
                .tokenType(type)
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
        userEmail = jwtService.extractEmail(refreshToken);
        if (userEmail != null) {
            User user = userService.findUserByEmail(userEmail);
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken, TokenType.ACCESS);
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }
}
