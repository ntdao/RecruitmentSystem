package com.recruitmentsystem.repository;

import com.recruitmentsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository extends JpaRepository<User, Integer> {
//    @Query(value = "select '*' from User where deleteFlag=false", nativeQuery = true)
//    List<User> findAll();
//    Optional<UserDTO> findUserById(Integer integer);
//    Optional<UserDTO> findUserByEmail(String email);
//    Integer countById();
}
