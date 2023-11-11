package com.recruitmentsystem.role;

import com.recruitmentsystem.common.exception.ResourceAlreadyExistsException;
import com.recruitmentsystem.common.exception.ResourceNotFoundException;
import com.recruitmentsystem.role.Role;
import com.recruitmentsystem.role.RoleMapper;
import com.recruitmentsystem.role.RoleRequestModel;
import com.recruitmentsystem.role.RoleResponseModel;
import com.recruitmentsystem.role.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleMapper roleMapper;
    private final RoleRepository roleRepository;

    public void addRole(RoleRequestModel request) {
        // check role_name
        if (roleRepository.existsRoleByRoleName(request.name())) {
            throw new ResourceAlreadyExistsException("Role name already taken");
        }

        // add role to db
        Role role = new Role(request.name());
        roleRepository.save(role);
    }

    public List<RoleResponseModel> findAllRoles() {
        return roleRepository.findAll()
                .stream()
                .map(roleMapper::roleToResponseModel)
                .collect(Collectors.toList());
    }

    public Role findRoleById(Integer id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role with id " + id + " does not exist"));
    }

    public RoleResponseModel findRoleResponseModelById(Integer id) {
        return roleMapper.roleToResponseModel(findRoleById(id));
    }

    public Role findRoleByName(String name) {
        return roleRepository.findByRoleName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Role with name " + name + " does not exist"));
    }

    public RoleResponseModel findRoleResponseModelByName(String name) {
        return roleMapper.roleToResponseModel(findRoleByName(name));
    }

    public void updateRole(Integer id, RoleRequestModel request) {
        // tim role theo id
        Role updateRole = findRoleById(id);

        // update role
        updateRole.setRoleName(request.name());
        updateRole.setRoleId(id);
        roleRepository.save(updateRole);
    }

    public void deleteRole(Integer id) {
        Role role = findRoleById(id);
        roleRepository.deleteById(id);
    }
}
