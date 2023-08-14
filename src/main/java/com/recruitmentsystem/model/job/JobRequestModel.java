package com.recruitmentsystem.model.job;

import com.recruitmentsystem.common.myEnum.JobLevel;
import lombok.Builder;

import java.time.Instant;

@Builder
public record JobRequestModel(
        String name,
        String companyName,
        String branchAddress,
        JobLevel jobLevel,
        String jobDescription,
        String jobRequirement,
        String salary,
        Instant expiresDate,
        String jobUrl,
        String categories
) {
}
