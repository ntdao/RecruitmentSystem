package com.recruitmentsystem.model.branch;

import lombok.Builder;

import java.time.Instant;

@Builder
public record BranchDisplayModel(
        Integer id,
        String name,
        String companyLogo,
        String branchAddress,
        Instant createdAt,
        Instant updatedAt
) {
}
