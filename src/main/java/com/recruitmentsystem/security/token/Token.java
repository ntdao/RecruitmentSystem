package com.recruitmentsystem.security.token;

import com.recruitmentsystem.common.myEnum.TokenType;
import com.recruitmentsystem.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Enumerated(EnumType.STRING)
    public TokenType tokenType;
    public boolean revoked = false;
    public boolean expired = false;
    @Column(nullable = false, unique = true)
    private String token;
//    @Column(nullable = false)
//    private Instant createdAt;
//    @Column(nullable = false)
//    private Instant expiresAt = Instant.now().plus(15, ChronoUnit.MINUTES);
    private Instant confirmedAt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    public boolean isExpired() {
        return expired;
    }

    public boolean isRevoked() {
        return revoked;
    }

//    public Token(String token,
//                             LocalDateTime createdAt,
//                             LocalDateTime expiresAt,
//                             User user) {
//        this.token = token;
//        this.createdAt = createdAt;
//        this.expiresAt = expiresAt;
//        this.user = user;
//    }
//    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE)
//    public Integer id;

//    @Column(unique = true)
//    public String token;
//
//    @Enumerated(EnumType.STRING)
//    public TokenType tokenType = TokenType.BEARER;
//
//    public boolean revoked;
//
//    public boolean expired;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id")
//    public User user;
}