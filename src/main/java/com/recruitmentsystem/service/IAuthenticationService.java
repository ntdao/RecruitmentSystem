package com.recruitmentsystem.service;

import com.recruitmentsystem.security.auth.AuthenticationRequest;
import com.recruitmentsystem.security.auth.AuthenticationResponse;
import com.recruitmentsystem.model.user.UserRequestModel;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface IAuthenticationService {
    AuthenticationResponse register(UserRequestModel request);

    AuthenticationResponse login(AuthenticationRequest request);

    AuthenticationResponse forgotPassword(String email);

//    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;

    String confirmToken(String token);

    void updatePassword(String token, String newPassword);
}
