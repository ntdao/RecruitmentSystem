package com.recruitmentsystem.job;

import lombok.Builder;

import java.time.Instant;

@Builder
public record JobRequestModel(
        String name,
        String company,
        String branch,
        String jobAddress,
        String jobPosition,
        String jobDescription,
        String jobRequirement,
        Integer salaryMax,
        Integer salaryMin,
        boolean isSalaryVisible,
        Instant jobDeadline,
        String jobUrl,
        String jobBenefit,
        Integer jobQuantity,
        String jobType,
        String jobTag,
        String jobGender,
        String jobStatus,
        String jobExperience,
        String category
) {
}
