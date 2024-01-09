package com.recruitmentsystem.security.token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface TokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findByToken(String token);

    @Query(value = """
            select t from Token t inner join Account a\s
            on t.account.id = a.id\s
            where a.id = :id and (t.expired = false or t.revoked = false)\s
            """)
    List<Token> findAllValidTokenByAccount(Integer id);

    @Query(value = """
            select t.token from Token t inner join Account a\s
            on t.account.id = a.id\s
            where a.id = :id and 
            t.tokenType = 'ACCESS' and
            (t.expired = false or t.revoked = false)\s
            """)
    String findAccessToken(Integer id);

    @Query(value = """
            select t.token from Token t inner join Account a\s
            on t.account.id = a.id\s
            where a.id = :id and 
            t.tokenType = 'REFRESH' and
            (t.expired = false or t.revoked = false)\s
            """)
    String findRefreshToken(Integer id);

    @Transactional
    @Modifying
    @Query("UPDATE Token c " +
            "SET c.confirmedAt = ?2 " +
            "WHERE c.token = ?1")
    int updateConfirmedAt(String token,
                          Instant confirmedAt);
}
