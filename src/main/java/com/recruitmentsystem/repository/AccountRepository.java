package com.recruitmentsystem.repository;

import com.recruitmentsystem.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    @Query("""
            select a 
            from Account  a 
            where a.email = :email and a.deleteFlag = false
            """)
    Optional<Account> findTopByEmail(String email);

    @Query("""
            select a.id 
            from Account  a 
            where a.email = :email and a.deleteFlag = false
            """)
    Integer findAccountIdByEmail(String email);

    boolean existsByEmailAndDeleteFlagFalse(String email);

    @Modifying
    @Query("""
            update Account a set a.enabled = true
            where a.email = :username
            """)
    void enableUser(String username);
}
