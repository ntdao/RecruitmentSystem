package com.recruitmentsystem.job;

import lombok.Builder;

import java.time.Instant;
import java.time.LocalDateTime;

@Builder
public record JobResponseModel(
        Integer id,
        String name,
        String companyLogo,
        String jobAddress,
        String jobPosition,
        String jobDescription,
        String jobRequirement,
//        String jobBenefit,
        Integer jobQuantity,
        String jobType,
        String jobTag,
        String jobGender,
        String jobStatus,
        String jobExperience,
        String salary,
        Instant jobExpiredDate,
        String jobUrl,
        String category,
        LocalDateTime createdAt,
        String branch,
        String companyName
) {
}
