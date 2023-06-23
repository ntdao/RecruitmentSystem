package com.recruitmentsystem.service;

import com.recruitmentsystem.entity.Role;
import com.recruitmentsystem.model.role.RoleDisplayModel;
import com.recruitmentsystem.model.role.RoleRequestModel;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IRoleService {
    void addRole(RoleRequestModel request);


    List<RoleDisplayModel> findAllRoles();

    RoleDisplayModel findById(Integer id);

    Role findRoleById(Integer id);

    Role findByRoleName(String name);

    @Transactional
    void updateRole(Integer id, RoleRequestModel request);

    void deleteRole(Integer id);
}
