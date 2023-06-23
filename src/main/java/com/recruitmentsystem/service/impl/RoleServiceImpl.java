package com.recruitmentsystem.service.impl;

import com.recruitmentsystem.common.exception.ResourceAlreadyExistsException;
import com.recruitmentsystem.common.exception.ResourceNotFoundException;
import com.recruitmentsystem.entity.Role;
import com.recruitmentsystem.mapper.RoleMapper;
import com.recruitmentsystem.model.role.RoleDisplayModel;
import com.recruitmentsystem.model.role.RoleRequestModel;
import com.recruitmentsystem.repository.IRoleRepository;
import com.recruitmentsystem.service.IRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements IRoleService {
    private final IRoleRepository roleRepository;
    private final RoleMapper roleMapper;
//    @Override
//    public Role findByRoleName(String roleName) {
//        return roleRepository.findByRoleName(roleName);
//    }
//
//    @Override
//    public List<Role> findAllRoles() {
//        return roleRepository.findAll();
//    }

    @Override
    public void addRole(RoleRequestModel request) {
        // check rolename
        String roleName = request.name();
        if(roleRepository.existsRoleByRoleName(roleName)){
            throw new ResourceAlreadyExistsException("role name already taken");
        }

        // add
        Role role = roleMapper.roleRequestModelToRole(request);
        role.setCreatedAt(Instant.now());
        roleRepository.save(role);
    }

    @Override
    public List<RoleDisplayModel> findAllRoles() {
        List<Role> roles = roleRepository.findAll();
        return roles.stream()
                .filter(role -> !role.isDeleteFlag())
                .map(roleMapper::roleToDisplayModel)
                .collect(Collectors.toList());
    }
    @Override
    public RoleDisplayModel findById(Integer id) {
        return roleRepository.findById(id)
                .filter(role -> !role.isDeleteFlag())
                .map(roleMapper::roleToDisplayModel)
                .orElseThrow(() -> new ResourceNotFoundException("Role with id " + id + " does not exist"));
    }

    @Override
    public Role findRoleById(Integer id) {
        return roleRepository.findById(id)
                .filter(role -> !role.isDeleteFlag())
                .orElseThrow(() -> new ResourceNotFoundException("Role with id " + id + " does not exist"));
    }

    @Override
    public Role findByRoleName(String name) {
        return roleRepository.findByRoleName(name)
                .filter(role -> !(role.isDeleteFlag() == true))
                .orElseThrow(() -> new ResourceNotFoundException("Role with name " + name + " does not exist"));
    }
    @Override
    @Transactional
    public void updateRole(Integer id, RoleRequestModel request) {
        // tim role theo id
        Role updateRole = findRoleById(id);
//        updateRole.setUpdatedAt(LocalDateTime.now());

        // tao ban ghi luu thong tin cu cua role
        Role oldRole = new Role(updateRole, id, true);
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
    @Override
    public void deleteRole(Integer id) {
        Role role = findRoleById(id);
        role.setDeleteFlag(true);
        roleRepository.save(role);
    }
}
