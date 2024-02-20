package com.recruitmentsystem.dto;

import com.recruitmentsystem.enums.Gender;
import lombok.Builder;

import java.util.List;

@Builder
public record JobRequestModel(
        String jobName,
        String jobType,
        String categoryId,
        String minEducationLevel,
        String jobExperience,
        Gender jobGender,
        String jobQuantity,
        String jobExpiredDate,
        String jobSalary,
        String jobDescription,
        String jobRequirement,
        String jobBenefit,
        List<String> jobSkills,
        List<AddressDto> jobAddresses,
        Integer size,
        Integer page
) {
}
