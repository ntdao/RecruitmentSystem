package com.recruitmentsystem.security.token;

import com.recruitmentsystem.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final ITokenRepository tokenRepository;

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
        return !tokenUser.isRevoked();
    }

    public List<Token> findAllValidTokenByUser(Integer id) {
        return tokenRepository.findAllValidTokenByUser(id);
    }

    public void saveAll(List<Token> validUserTokens) {
        tokenRepository.saveAll(validUserTokens);
    }
}
