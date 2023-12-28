package com.recruitmentsystem.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recruitmentsystem.entity.Role;
import com.recruitmentsystem.exception.ResourceAlreadyExistsException;
import com.recruitmentsystem.exception.ResourceNotFoundException;
import com.recruitmentsystem.repository.RoleRepository;
import com.recruitmentsystem.dto.RoleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final ObjectMapper objectMapper;
    private final RoleRepository roleRepository;

    public void addRole(RoleDto request) {
        // check role_name
        if (roleRepository.existsRoleByRoleName(request.roleName())) {
            throw new ResourceAlreadyExistsException("Role name already taken");
        }

        // add role to db
        Role role = new Role(request.roleName());
        roleRepository.save(role);
    }

    public List<RoleDto> findAllRoles() {
        return roleRepository.findAll()
                .stream()
                .map(r -> objectMapper.convertValue(r, RoleDto.class))
                .collect(Collectors.toList());
    }

    public Role findRoleById(Integer id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role with id " + id + " does not exist"));
    }

    public RoleDto findRoleResponseModelById(Integer id) {
        return objectMapper.convertValue(findRoleById(id), RoleDto.class);
    }

    public Role findRoleByName(String name) {
        return roleRepository.findByRoleName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Role with name " + name + " does not exist"));
    }

    public RoleDto findRoleResponseModelByName(String name) {
        return objectMapper.convertValue(findRoleByName(name), RoleDto.class);
    }

    public void updateRole(Integer id, RoleDto request) {
        // tim role theo id
        Role updateRole = findRoleById(id);

        // update role
        updateRole.setRoleName(request.roleName());
        updateRole.setRoleId(id);
        roleRepository.save(updateRole);
    }

    public void deleteRole(Integer id) {
        Role role = findRoleById(id);
        roleRepository.deleteById(id);
    }
}
