package com.recruitmentsystem.repository;

import com.recruitmentsystem.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findById(Integer integer);

    Optional<Role> findByRoleName(String roleName);

    boolean existsRoleByRoleName(String roleName);

}
