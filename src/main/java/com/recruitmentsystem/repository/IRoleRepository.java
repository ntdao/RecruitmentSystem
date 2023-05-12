package com.recruitmentsystem.repository;

import com.recruitmentsystem.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRoleRepository extends JpaRepository<Role, Integer> {
    Role findRoleByRoleName(String roleName);
}
