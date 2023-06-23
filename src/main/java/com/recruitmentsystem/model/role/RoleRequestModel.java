package com.recruitmentsystem.model.role;

import java.time.Instant;

public record RoleRequestModel(
        String name,
        Instant createdAt,
        Instant updatedAt
) {
}
