package com.recruitmentsystem.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record JobResponseModel(
        Integer jobId,
        String jobName,
        String minEducationLevel,
        String jobExperience,
        String jobGender,
        Integer jobQuantity,
        Integer jobCandidate,
        Integer jobPass,
        String salary,
        String jobDescription,
        String jobRequirement,
        String jobBenefit,
        String companyName,
        String companyLogo,
        JobTypeDto jobType,
        Integer jobStatus,
        LocalDateTime createdAt,
        LocalDateTime jobExpiredDate,
        CategoryDto category,
        List<SkillDto> jobSkill,
        List<AddressDto> jobAddress
) {
}
