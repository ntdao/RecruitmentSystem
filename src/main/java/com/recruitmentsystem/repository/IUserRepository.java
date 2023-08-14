package com.recruitmentsystem.repository;

import com.recruitmentsystem.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User, Integer>, PagingAndSortingRepository<User, Integer> {
    boolean existsUserByUsername(String username);

    boolean existsUserByEmail(String email);

    List<User> findUserByUsername(String username);

    Optional<User> findTopByEmail(String email);

//    @Transactional
    @Modifying
    @Query("UPDATE User u " +
            "SET u.enabled = TRUE WHERE u.email = ?1")
    int enableUser(String email);

    @Query("Select count(*) from User u where u.deleteFlag = false")
    int countAllUser();
}
