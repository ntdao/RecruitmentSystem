package hust.seee.recruitmentsystem.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import hust.seee.recruitmentsystem.dto.CandidateDTO;
import hust.seee.recruitmentsystem.entity.Account;
import hust.seee.recruitmentsystem.entity.Candidate;
import hust.seee.recruitmentsystem.enums.TokenType;
import hust.seee.recruitmentsystem.exception.ResourceNotFoundException;
import hust.seee.recruitmentsystem.mapper.CandidateMapper;
import hust.seee.recruitmentsystem.repository.AccountRepository;
import hust.seee.recruitmentsystem.repository.CandidateRepository;
import hust.seee.recruitmentsystem.request.AuthRequest;
import hust.seee.recruitmentsystem.response.AuthResponse;
import hust.seee.recruitmentsystem.security.jwt.JwtService;
import hust.seee.recruitmentsystem.security.token.Token;
import hust.seee.recruitmentsystem.security.token.TokenService;
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
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AccountRepository accountRepository;
    private final AccountService accountService;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final TokenService tokenService;
    private final CandidateMapper candidateMapper;
    private final CandidateRepository candidateRepository;

    public void register(CandidateDTO request) {
        accountService.checkDuplicateEmail(request.getEmail());

        Account account = Account.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(roleService.findByName(request.getRoleName()))
                .build();
        accountRepository.save(account);

        Candidate candidate = candidateMapper.dtoToEntity(request);
        candidate.setCreatedBy(account.getId());

        candidateRepository.save(candidate);

        String token = jwtService.generateEmailToken(account);
        saveAccountToken(account, token, TokenType.EMAIL);

        String link = "http://localhost:3000/register/confirm?token=" + token;
        emailService.sendConfirmEmail(request.getFullName(), request.getEmail(), link);
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
        accountRepository.enableUser(confirmationToken.getAccount().getUsername());
    }

    public AuthResponse login(AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(), request.getPassword())
        );
        Account account = (Account) authentication.getPrincipal();
        if (!account.isEnabled()) {
            throw new IllegalStateException("Your account has not been enabled!");
        } else if (account.isDeleteFlag()) {
            throw new IllegalStateException("Your account does not exist!");
        }

        var accessToken = jwtService.generateToken(account);
        var refreshToken = jwtService.generateRefreshToken(account);
        revokeAllAccountTokens(account);
        saveAccountToken(account, accessToken, TokenType.ACCESS);
        saveAccountToken(account, refreshToken, TokenType.REFRESH);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthResponse forgotPassword(String email) {
        Account account = accountService.findAccountByEmail(email);
        String token = jwtService.generateEmailToken(account);
        saveAccountToken(account, token, TokenType.EMAIL);

        String link = "http://localhost:3000/reset-password?token=" + token;
        emailService.sendResetPasswordEmail(account.getUsername(), account.getUsername(), link);

        return AuthResponse.builder()
                .accessToken(token)
                .build();
    }

    public void updatePassword(String token, String newPassword) {
        confirmEmail(token);
        Account account = accountService.findAccountByToken(token);
        revokeAllAccountTokens(account);
        account.setPassword(passwordEncoder.encode(newPassword));
        accountRepository.save(account);
    }

    public void saveAccountToken(Account account, String jwtToken, TokenType type) {
        Token token = Token
                .builder()
                .account(account)
                .token(jwtToken)
                .tokenType(type)
                .expired(false)
                .revoked(false)
                .build();
        tokenService.saveToken(token);
    }

    private void revokeAllAccountTokens(Account account) {
        List<Token> validUserTokens = tokenService.findAllValidTokenByAccount(account.getId());
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
    ) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String email;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        email = jwtService.extractEmail(refreshToken);
        if (email != null) {
            Account account = accountService.findAccountByEmail(email);
            if (jwtService.isTokenValid(refreshToken, account)) {
                var accessToken = jwtService.generateToken(account);
                revokeAllAccountTokens(account);
                saveAccountToken(account, accessToken, TokenType.ACCESS);
                var authResponse = AuthResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                try {
                    new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
