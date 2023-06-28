package com.recruitmentsystem.controller;

import com.recruitmentsystem.common.exception.ResourceAlreadyExistsException;
import com.recruitmentsystem.model.TestResponse;
import com.recruitmentsystem.model.user.UserRequestModel;
import com.recruitmentsystem.security.auth.AuthenticationRequest;
import com.recruitmentsystem.security.auth.AuthenticationResponse;
import com.recruitmentsystem.service.IAuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final IAuthenticationService authenticationService;

    @PostMapping("/register")
    public TestResponse<?> register(
            @RequestBody UserRequestModel request) {
        AuthenticationResponse response;
        try {
            response = authenticationService.register(request);
        } catch (ResourceAlreadyExistsException e) {
            return new TestResponse<>(-1, e.getMessage());
        }
        return new TestResponse(0, response);
    }

    @GetMapping(path = "/register/confirm")
    public TestResponse<?> confirm(@RequestParam("token") String token) {
        try {
            authenticationService.confirmEmail(token);
        } catch (Exception e) {
            return new TestResponse<>(1, e.getMessage());
        }
        return new TestResponse<>(0);
    }

    @PostMapping("/login")
    public TestResponse<?> login(@RequestBody AuthenticationRequest request) {
        AuthenticationResponse response;
        try {
            response = authenticationService.login(request);
        } catch (ResourceAlreadyExistsException e) {
            return new TestResponse<>(-1, e.getMessage());
        }
        return new TestResponse(0, response);
    }

    @PostMapping("/refresh-token")
    public TestResponse<?> refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        try {
            authenticationService.refreshToken(request, response);
        } catch (IOException e) {
            return new TestResponse<>(1, e.getMessage());
        }
        return new TestResponse<>(0);
    }

    @PostMapping("/forgot_password")
    public TestResponse<?> forgotPassword(@RequestBody String email) {
        AuthenticationResponse response;
        try {
            response = authenticationService.forgotPassword(email);
        } catch (Exception e) {
            return new TestResponse(1, e.getMessage());
        }
        return new TestResponse<>(0, response);
    }

    @GetMapping("/forgot_password/confirm")
    public TestResponse<?> processForgotPassword(@RequestParam("token") String token) {
        try {
            authenticationService.confirmEmail(token);
        } catch (Exception e) {
            return new TestResponse<>(1, e.getMessage());
        }
        return new TestResponse<>(0);
    }

    @PostMapping("/reset_password")
    public TestResponse<?> processResetPassword(@RequestParam("token") String token,
                                                @RequestBody String newPassword) {
        try {
            authenticationService.updatePassword(token, newPassword);
        } catch (Exception e) {
            return new TestResponse<>(1, e.getMessage());
        }
        return new TestResponse<>(0);
    }
}
