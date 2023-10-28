package com.recruitmentsystem.service;

import com.recruitmentsystem.model.auth.AuthenticationRequest;
import com.recruitmentsystem.model.auth.AuthenticationResponse;
import com.recruitmentsystem.model.user.UserRequestModel;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface IAuthenticationService {
    void register(UserRequestModel request);

    AuthenticationResponse login(AuthenticationRequest request);

    AuthenticationResponse forgotPassword(String email);

    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;

    void confirmEmail(String token);

    void updatePassword(String token, String newPassword);

//    void revokeAllUserTokens(User updateUser);
}
