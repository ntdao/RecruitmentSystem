package com.recruitmentsystem.model.job;

import lombok.Builder;

import java.time.Instant;

@Builder
public record JobDisplayModel(
        String name,
        String companyLogo,
        String jobAddress,
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
