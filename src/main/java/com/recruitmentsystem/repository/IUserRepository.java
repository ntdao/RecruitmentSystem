package com.recruitmentsystem.repository;

import com.recruitmentsystem.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User, Integer> {
    boolean existsUserByUsername(String username);

    boolean existsUserByEmail(String email);

    Optional<User> findUserByUsername(String username);

    Optional<User> findTopByEmail(String email);

    @Transactional
    @Modifying
    @Query("UPDATE User u " +
            "SET u.deleteFlag = FALSE WHERE u.email = ?1")
    int enableUser(String email);
}
