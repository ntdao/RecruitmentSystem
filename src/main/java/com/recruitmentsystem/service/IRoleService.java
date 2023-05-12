package com.recruitmentsystem.service;

import com.recruitmentsystem.entity.Role;

public interface IRoleService {
    Role findRoleByRoleName(String roleName);
}
