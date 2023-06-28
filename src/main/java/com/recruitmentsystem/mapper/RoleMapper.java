package com.recruitmentsystem.mapper;

import com.recruitmentsystem.entity.Role;
import com.recruitmentsystem.model.role.RoleDisplayModel;
import com.recruitmentsystem.model.role.RoleRequestModel;
import org.springframework.stereotype.Service;

@Service
public class RoleMapper {
    public RoleDisplayModel roleToDisplayModel(Role role) {
        return RoleDisplayModel
                .builder()
                .id(role.getRoleId())
                .name(role.getRoleName())
                .createdAt(role.getCreatedAt())
                .updatedAt(role.getUpdatedAt())
                .build();
    }

    public Role roleRequestModelToRole(RoleRequestModel request) {
        return Role
                .builder()
                .roleName(request.name())
                .build();
    }
}
