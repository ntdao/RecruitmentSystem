package com.recruitmentsystem.service;

import com.recruitmentsystem.common.exception.ResourceAlreadyExistsException;
import com.recruitmentsystem.common.exception.ResourceNotFoundException;
import com.recruitmentsystem.entity.Role;
import com.recruitmentsystem.mapper.RoleMapper;
import com.recruitmentsystem.model.role.RoleDisplayModel;
import com.recruitmentsystem.model.role.RoleRequestModel;
import com.recruitmentsystem.repository.IRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final IRoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public void addRole(RoleRequestModel request) {
        // check rolename
        String roleName = request.name();
        if (roleRepository.existsRoleByRoleName(roleName)) {
            throw new ResourceAlreadyExistsException("role name already taken");
        }

        // add
        Role role = roleMapper.roleRequestModelToRole(request);
        role.setCreatedAt(Instant.now());
        roleRepository.save(role);
    }

    public List<RoleDisplayModel> findAllRoles() {
        List<Role> roles = roleRepository.findAll();
        return roles.stream()
                .filter(role -> !role.isDeleteFlag())
                .map(roleMapper::roleToDisplayModel)
                .collect(Collectors.toList());
    }

    public RoleDisplayModel findById(Integer id) {
        return roleRepository.findById(id)
                .filter(role -> !role.isDeleteFlag())
                .map(roleMapper::roleToDisplayModel)
                .orElseThrow(() -> new ResourceNotFoundException("Role with id " + id + " does not exist"));
    }

    public Role findRoleById(Integer id) {
        return roleRepository.findById(id)
                .filter(role -> !role.isDeleteFlag())
                .orElseThrow(() -> new ResourceNotFoundException("Role with id " + id + " does not exist"));
    }

    public Role findByRoleName(String name) {
        return roleRepository.findByRoleName(name)
                .filter(role -> !(role.isDeleteFlag()))
                .orElseThrow(() -> new ResourceNotFoundException("Role with name " + name + " does not exist"));
    }

    @Transactional
    public void updateRole(Integer id, RoleRequestModel request) {
        // tim role theo id
        Role updateRole = findRoleById(id);
//        updateRole.setUpdatedAt(LocalDateTime.now());

        // tao ban ghi luu thong tin cu cua role
        Role oldRole = new Role(updateRole, true);
        roleRepository.save(oldRole);

        // update role
        updateRole = roleMapper.roleRequestModelToRole(request);
        updateRole.setRoleId(id);
        updateRole.setCreatedAt(oldRole.getCreatedAt());
        updateRole.setCreatedBy(oldRole.getCreatedBy());
        updateRole.setUpdatedAt(oldRole.getUpdatedAt());
//        updateRole.setUpdatedBy();
        roleRepository.save(updateRole);
    }

    public void deleteRole(Integer id) {
        Role role = findRoleById(id);
        role.setDeleteFlag(true);
        roleRepository.save(role);
    }
}
