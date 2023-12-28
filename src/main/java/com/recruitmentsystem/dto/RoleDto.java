package com.recruitmentsystem.dto;

import lombok.Builder;

@Builder
public record RoleDto(
        Integer roleId,
        String roleName
) {
}
