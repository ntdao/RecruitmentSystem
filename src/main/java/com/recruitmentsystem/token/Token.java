package com.recruitmentsystem.token;

import com.recruitmentsystem.common.myEnum.TokenType;
import com.recruitmentsystem.account.Account;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Token {
    @Enumerated(EnumType.STRING)
    public TokenType tokenType;
    public boolean revoked = false;
    public boolean expired = false;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false, unique = true)
    private String token;
    //    @Column(nullable = false)
//    private Instant createdAt;
//    @Column(nullable = false)
//    private Instant expiresAt = Instant.now().plus(15, ChronoUnit.MINUTES);
    private Instant confirmedAt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable=false, name="account_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Account account;

//    public boolean isExpired() {
//        return expired;
//    }

//    public boolean isRevoked() {
//        return revoked;
//    }

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