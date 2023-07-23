package com.recruitmentsystem.model.job;

import lombok.Builder;

import java.time.Instant;

@Builder
public record JobRequestModel(
        String name,
        String companyLogo,
        String branchAddress,
        String jobLevel,
        String jobDescription,
        String jobRequirement,
        String salary,
        Instant expiresDate,
        String jobUrl,
        String category,
        Instant createdAt
) {
}
