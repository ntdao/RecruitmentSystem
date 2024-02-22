package hust.seee.recruitmentsystem.security.config;

import hust.seee.recruitmentsystem.entity.Account;
import hust.seee.recruitmentsystem.security.jwt.JwtService;
import hust.seee.recruitmentsystem.service.AccountService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {
    private final AccountService accountService;
    private final JwtService jwtService;

    @Override
    public void logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String email;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        jwt = authHeader.substring(7);
        email = jwtService.extractEmail(jwt);

        if (email != null) {
            Account account = accountService.findAccountByEmail(email);
            accountService.revokeAllAccountTokens(account.getId());
            SecurityContextHolder.clearContext();
        }
//        var storedToken = tokenRepository.findByToken(jwt).orElse(null);
//        if (storedToken != null) {
//            storedToken.setExpired(true);
//            storedToken.setRevoked(true);
//            tokenRepository.save(storedToken);
//            SecurityContextHolder.clearContext();
//        }
    }
}
