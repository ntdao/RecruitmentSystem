package com.recruitmentsystem.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("select u from User u " +
            "left join Account a on u.account.id = a.id " +
            "join fetch u.userEducations " +
            "join fetch u.workingHistories " +
            "where a.deleteFlag = false " +
            "and u.deleteFlag = false " +
            "and a.role.roleId = 3")
    List<User> findAllUser();

    @Query("select u from User u " +
            "left join Account a on u.account.id = a.id " +
            "where a.deleteFlag = false and a.role.roleId = 3")
    Page<User> findAllUser(Pageable paging);

    @Query("select u from User u " +
            "left join Account a on u.account.id = a.id " +
            "join fetch u.userEducations " +
            "join fetch u.workingHistories " +
            "where u.deleteFlag = false " +
            "and a.deleteFlag = false " +
            "and a.email = ?1")
    Optional<User> findUserByEmail(String email);
    @Query("select u from User u " +
            "left join Account a on u.account.id = a.id " +
            "join fetch u.userEducations " +
            "where u.deleteFlag = false " +
            "and a.deleteFlag = false " +
            "and a.email = ?1")
    Optional<User> findUserEducations(String email);
    @Query("select u from User u " +
            "join fetch u.workingHistories " +
            "where u in :user")
    Optional<User> findUserWorkingHistories(User user);

    @Query("select count(*) from User u" +
            " where u.deleteFlag = false " +
            "and u.account.role.roleId = 3")
    int countAllUser();

    @Query("""
            select u from User u 
            where u.fullName like :name%
            and u.deleteFlag = false
            and u.account.role.roleId = 3
            """
    )
    List<User> findAllUserByName(String name);
}
