package com.recruitmentsystem.account;

import com.recruitmentsystem.common.exception.ResourceAlreadyExistsException;
import com.recruitmentsystem.common.exception.ResourceNotFoundException;
import com.recruitmentsystem.security.jwt.JwtService;
import com.recruitmentsystem.token.Token;
import com.recruitmentsystem.token.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final JwtService jwtService;
    private final TokenService tokenService;

    public Account getCurrentAccount(Principal connectedUser) {
        return (Account) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
    }

    public Account findAccountByEmail(String email) {
        return accountRepository.findTopByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Account with email " + email + " not found!"));
    }

    public Account findAccountByToken(String token) {
        boolean isValid = tokenService.isValidToken(token);
        Account account = null;
        if (isValid) {
            String email = jwtService.extractEmail(token);
            System.out.println(email);
            try {
                account = findAccountByEmail(email);
                System.out.println(account);
            } catch (Exception ex) {
                throw new ResourceNotFoundException("Account with token " + token + " does not exist");
            }
        } else {
            throw new ResourceNotFoundException("Token is invalid");
        }
        return account;
    }

    public void checkDuplicateEmail(String email) {
        if (accountRepository.existsByEmailAndDeleteFlagFalse(email)) {
            throw new ResourceAlreadyExistsException("email already taken");
        }
//        Integer id = accountRepository.findAccountIdByEmail(email);
//        if (id != null) {
//            throw new ResourceNotFoundException("Email already taken");
//        }
    }

    public void revokeAllAccountTokens(Integer id) {
        List<Token> validAccountTokens = tokenService.findAllValidTokenByAccount(id);
        if (validAccountTokens.isEmpty()) return;
        validAccountTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenService.saveAll(validAccountTokens);
    }
}
