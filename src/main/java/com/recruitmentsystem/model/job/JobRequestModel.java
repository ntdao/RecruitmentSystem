package com.recruitmentsystem.model.job;

import com.recruitmentsystem.common.myEnum.JobLevel;
import lombok.Builder;

import java.time.Instant;

@Builder
public record JobRequestModel(
        String name,
        String company,
        String branch,
        String jobAddress,
        JobLevel jobLevel,
        String jobDescription,
        String jobRequirement,
        Integer salaryMax,
        Integer salaryMin,
        Instant jobDeadline,
        String jobUrl,
        String jobBenefit,
        Integer jobQuantity,
        String jobType,
        String jobTag,
        String jobGender,
        String jobStatus,
        String jobExperience,
        String categories
) {
}
