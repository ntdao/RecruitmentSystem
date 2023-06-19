package com.recruitmentsystem.service.impl;

import com.recruitmentsystem.entity.Role;
import com.recruitmentsystem.repository.IRoleRepository;
import com.recruitmentsystem.service.IRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements IRoleService {
    private final IRoleRepository roleRepository;
    @Override
    public Role findByRoleName(String roleName) {
        return roleRepository.findByRoleName(roleName);
    }

    @Override
    public List<Role> findAllRoles() {
        return roleRepository.findAll();
    }
}
