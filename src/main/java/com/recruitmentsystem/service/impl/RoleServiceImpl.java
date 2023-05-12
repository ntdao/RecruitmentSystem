package com.recruitmentsystem.service.impl;

import com.recruitmentsystem.entity.Role;
import com.recruitmentsystem.repository.IRoleRepository;
import com.recruitmentsystem.service.IRoleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RoleServiceImpl implements IRoleService {
    private final IRoleRepository roleRepository;
    @Override
    public Role findRoleByRoleName(String roleName) {
        return roleRepository.findRoleByRoleName(roleName);
    }
}
