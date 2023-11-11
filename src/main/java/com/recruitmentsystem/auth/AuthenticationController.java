package com.recruitmentsystem.auth;

import com.recruitmentsystem.user.UserRequestModel;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@RequestBody UserRequestModel request) {
        authenticationService.register(request);
    }

    @GetMapping(path = "/register/confirm")
    public void confirm(@RequestParam("token") String token) {
        authenticationService.confirmEmail(token);
    }

    @PostMapping("/login")
//    @ResponseStatus(HttpStatus.OK)
    public AuthenticationResponseModel login(@RequestBody AuthenticationRequestModel request) {
        return authenticationService.login(request);
    }

    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        authenticationService.refreshToken(request, response);
    }

    @PostMapping("/forgot-password")
    public AuthenticationResponseModel forgotPassword(@RequestBody Object request) {
        String email = request.toString().substring(7, request.toString().length() - 1);
        return authenticationService.forgotPassword(email);
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

    @PostMapping("/reset-password")
    public void processResetPassword(@RequestParam("token") String token,
                                     @RequestBody Object newPassword) {
        String password = newPassword.toString().substring(10, newPassword.toString().length() - 1);
        authenticationService.updatePassword(token, password);
    }
}
