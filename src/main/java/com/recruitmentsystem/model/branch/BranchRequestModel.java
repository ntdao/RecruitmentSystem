package com.recruitmentsystem.model.branch;

import java.time.Instant;

public record BranchRequestModel(
        String name,
        String branchAddress,
        Instant createdAt,
        Instant updatedAt
) {
}
