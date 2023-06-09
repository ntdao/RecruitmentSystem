package com.recruitmentsystem.model.role;

import lombok.Builder;

import java.time.Instant;

@Builder
public record RoleDisplayModel(
        Integer id,
        String name,
        String imgUrl,
        Instant createdAt,
        Instant updatedAt
) {
}
