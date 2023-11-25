package com.recruitmentsystem.job;

import com.recruitmentsystem.address.address.AddressRequestModel;
import lombok.Builder;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Builder
public record JobRequestModel(
        String name,
        String company,
        String branch,
        List<AddressRequestModel> jobAddresses,
        String jobPosition,
        String jobDescription,
        String jobRequirement,
        Integer salaryMax,
        Integer salaryMin,
        boolean isSalaryVisible,
        LocalDateTime jobExipredDate,
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
