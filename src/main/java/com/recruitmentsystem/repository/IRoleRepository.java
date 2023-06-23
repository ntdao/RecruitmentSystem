package com.recruitmentsystem.repository;

import com.recruitmentsystem.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.stream.DoubleStream;

@Repository
public interface IRoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findById(Integer integer);

    Optional<Role> findByRoleName (String roleName);

    boolean existsRoleByRoleName(String roleName);

}
