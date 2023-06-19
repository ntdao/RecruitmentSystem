package com.recruitmentsystem.controller;

import com.recruitmentsystem.security.auth.AuthenticationRequest;
import com.recruitmentsystem.model.user.UserRequestModel;
import com.recruitmentsystem.service.IAuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final IAuthenticationService authenticationService;
    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody UserRequestModel request) {
        return ResponseEntity.ok().body(authenticationService.register(request));
    }

    @GetMapping(path = "/register/confirm")
    public ResponseEntity<?> confirm(@RequestParam("token") String token) {
        return ResponseEntity.ok().body(authenticationService.confirmToken(token));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok().body(authenticationService.login(request));
    }

//    @PostMapping("/refresh-token")
//    public ResponseEntity<?> refreshToken(
//            HttpServletRequest request,
//            HttpServletResponse response
//    ) throws IOException {
//        ResponseEntity.ok().body(authenticationService.refreshToken(request, response));
//    }

    @PostMapping("/forgot_password")
    public ResponseEntity<?> forgotPassword(@RequestBody String email) {
        return ResponseEntity.ok().body(authenticationService.forgotPassword(email));
    }

    @GetMapping("/forgot_password/confirm")
    public ResponseEntity<?> processForgotPassword(@RequestParam("token") String token) {
        return ResponseEntity.ok().body(authenticationService.confirmToken(token));
    }

    @PostMapping("/reset_password")
    public void processResetPassword(@RequestParam("token") String token,
                                                  @RequestBody String newPassword) {
        authenticationService.updatePassword(token, newPassword);
    }
}
