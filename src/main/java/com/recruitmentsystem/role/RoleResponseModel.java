package com.recruitmentsystem.role;

import lombok.Builder;

@Builder
public record RoleResponseModel(
        Integer id,
        String name
) {
}
