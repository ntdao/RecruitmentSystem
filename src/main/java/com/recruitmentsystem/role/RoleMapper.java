package com.recruitmentsystem.role;

import org.springframework.stereotype.Service;

@Service
public class RoleMapper {
    public RoleResponseModel roleToResponseModel(Role role) {
        return RoleResponseModel
                .builder()
                .id(role.getRoleId())
                .name(role.getRoleName())
                .build();
    }

    public Role roleRequestModelToRole(RoleRequestModel request) {
        return Role
                .builder()
                .roleName(request.name())
                .build();
    }
}
