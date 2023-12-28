package com.recruitmentsystem.repository;

import com.recruitmentsystem.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("""
            select u from User u
            left join fetch u.userEducations
            left join fetch u.workingHistories
            left join fetch u.userSkills
            join fetch u.account
            where u.account.deleteFlag = false
            and u.deleteFlag = false
            """)
    List<User> findAllUser();

    @Query(value = """
            select u from User u 
            left join fetch u.userEducations 
            left join fetch u.workingHistories
            left join fetch u.userSkills
            join fetch u.account
            where u.account.deleteFlag = false
            """,
            countQuery = """
            select u from User u 
            left join fetch u.userEducations 
            left join fetch u.workingHistories
            left join fetch u.userSkills
            where u.account.deleteFlag = false
            """)
    Page<User> findAllUser(Pageable paging);

    @Query("""
            select u from User u
            left join fetch u.userEducations
            left join fetch u.workingHistories
            left join fetch u.userSkills
            join fetch u.account
            where u.deleteFlag = false
            and u.account.deleteFlag = false
            and u.account.email = :email
            """)
    Optional<User> findUserByEmail(String email);

    @Query("""
            select u from User u 
            left join fetch u.userEducations
            left join fetch u.workingHistories 
            left join fetch u.userSkills
            join fetch u.account
            where u.fullName like %:name%
            and u.deleteFlag = false
            """)
    List<User> findAllUserByName(String name);

    @Query("""
            select u from User u
            left join fetch u.userEducations
            left join fetch u.workingHistories
            left join fetch u.userSkills
            join fetch u.account
            where u.deleteFlag = false
            and u.account.deleteFlag = false
            and u.userId = :id
            """)
    Optional<User> findUserById(Integer id);
}
