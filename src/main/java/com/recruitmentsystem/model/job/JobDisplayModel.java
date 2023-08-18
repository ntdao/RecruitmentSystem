package com.recruitmentsystem.model.job;

import com.recruitmentsystem.common.myEnum.JobLevel;
import lombok.Builder;

import java.time.Instant;
import java.util.List;

@Builder
public record JobDisplayModel(
        String name,
        String companyLogo,
        String jobAddress,
        JobLevel jobLevel,
        String jobDescription,
        String jobRequirement,
        String salary,
        Instant expiresDate,
        String jobUrl,
        List<String> category,
        Instant createdAt,
        String branch,
        String companyName
) {
}
