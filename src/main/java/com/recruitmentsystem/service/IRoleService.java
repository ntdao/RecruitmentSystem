package com.recruitmentsystem.service;

import com.recruitmentsystem.entity.Role;

import java.util.List;

public interface IRoleService {
    Role findByRoleName(String roleName);

    List<Role> findAllRoles();
}
