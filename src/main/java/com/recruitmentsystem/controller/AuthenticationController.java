package com.recruitmentsystem.controller;

import com.recruitmentsystem.common.exception.ResourceAlreadyExistsException;
import com.recruitmentsystem.model.user.UserRequestModel;
import com.recruitmentsystem.security.auth.AuthenticationRequest;
import com.recruitmentsystem.security.auth.AuthenticationResponse;
import com.recruitmentsystem.service.IAuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final IAuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody UserRequestModel request) {
        AuthenticationResponse response;
        try {
            response = authenticationService.register(request);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/register/confirm")
    public ResponseEntity<?> confirm(@RequestParam("token") String token) {
        try {
            authenticationService.confirmEmail(token);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest request) {
        AuthenticationResponse response;
        try {
            response = authenticationService.login(request);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        try {
            authenticationService.refreshToken(request, response);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());

        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/forgot_password")
    public ResponseEntity<?> forgotPassword(@RequestBody Object request) {
        AuthenticationResponse response;
        String email = request.toString().substring(7, request.toString().length() - 1);
        try {
            response = authenticationService.forgotPassword(email);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

//    @GetMapping("/forgot_password/confirm")
//    public ResponseEntity<?> processForgotPassword(@RequestParam("token") String token) {
//        try {
//            authenticationService.confirmEmail(token);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//        }
//        return ResponseEntity.ok().build();
//    }

    @PostMapping("/reset_password")
    public ResponseEntity<?> processResetPassword(@RequestParam("token") String token,
                                                @RequestBody Object newPassword) {
        try {
            String password = newPassword.toString().substring(10, newPassword.toString().length()-1);
            authenticationService.updatePassword(token, password);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }
    @GetMapping(value = "/welcome", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String welcomeAsHTML() {
        return "<html>\n" + "<header><title>Welcome</title></header>\n" +
                "<body>\n" + "Hello world\n" + "</body>\n" + "</html>";
    }
}
