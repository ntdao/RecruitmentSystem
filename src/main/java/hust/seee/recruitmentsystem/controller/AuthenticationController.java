package hust.seee.recruitmentsystem.controller;

import hust.seee.recruitmentsystem.dto.CandidateDTO;
import hust.seee.recruitmentsystem.request.AuthRequest;
import hust.seee.recruitmentsystem.response.AuthResponse;
import hust.seee.recruitmentsystem.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@RequestBody CandidateDTO request) {
        authenticationService.register(request);
    }

    @GetMapping(path = "/register/confirm")
    public void confirm(@RequestParam("token") String token) {
        authenticationService.confirmEmail(token);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {
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
    public AuthResponse forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
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
                                     @RequestBody Map<String, String> request) {
        String password = request.get("password");
        authenticationService.updatePassword(token, password);
    }
}
