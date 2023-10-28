package com.recruitmentsystem.model.job;

import lombok.Builder;

import java.time.Instant;
import java.util.List;

@Builder
public record JobDisplayModel(
        Integer id,
        String name,
        String companyLogo,
        String jobAddress,
        String jobLevel,
        String jobDescription,
        String jobRequirement,
        String jobBenefit,
        Integer jobQuantity,
        String jobType,
        String jobTag,
        String jobGender,
        String jobStatus,
        String jobExperience,
        String salary,
        Instant jobDeadline,
        String jobUrl,
        List<String> category,
        Instant createdAt,
        String branch,
        String companyName
) {
}
