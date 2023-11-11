package com.recruitmentsystem.user;

import com.recruitmentsystem.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>, PagingAndSortingRepository<User, Integer> {
//    boolean existsUserByUsername(String username);

//    boolean existsUserByEmail(String email);
    @Query(value = "select u from User u " +
            "left join Account a on u.account.id = a.id " +
            "where a.deleteFlag = false and a.role.roleId = 3")
    List<User> findAllUser();
    @Query(value = "select u from User u " +
            "left join Account a on u.account.id = a.id " +
            "where u.deleteFlag = false and a.email = ?1")
    Optional<User> findUserByEmail(String email);

//    @Modifying
//    @Query("UPDATE User a " +
//            "SET a.enabled = TRUE WHERE a.email = ?1")
//    int enableUser(String email);

//    @Query("Select count(*) from User u where u.deleteFlag = false")
    @Query("select count(*) from User u")
    int countAllUser();

    @Query("select max(userId) from User")
    Integer getUserIdRequest();
}
