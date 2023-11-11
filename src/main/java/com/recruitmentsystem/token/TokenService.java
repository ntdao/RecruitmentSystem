package com.recruitmentsystem.token;

import com.recruitmentsystem.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenRepository tokenRepository;

    public void saveToken(Token token) {
        tokenRepository.save(token);
    }

    public Optional<Token> getToken(String token) {
        return tokenRepository.findByToken(token);
    }

    public void setConfirmedAt(String token) {
        tokenRepository.updateConfirmedAt(
                token, Instant.now());
    }

    public boolean isValidToken(String token) {
        Token tokenUser = tokenRepository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Token does not exist"));
        return !tokenUser.isExpired() && !tokenUser.isRevoked();
    }

    public List<Token> findAllValidTokenByAccount(Integer id) {
        return tokenRepository.findAllValidTokenByAccount(id);
    }

    public String findAccessTokenByAccount(Integer id) {
        return tokenRepository.findAccessToken(id);
    }

    public String findRefreshTokenByAccount(Integer id) {
        return tokenRepository.findRefreshToken(id);
    }

    public void saveAll(List<Token> validAccountTokens) {
        tokenRepository.saveAll(validAccountTokens);
    }
}
