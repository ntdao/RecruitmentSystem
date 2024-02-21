package com.recruitmentsystem.dto;

import com.recruitmentsystem.enums.Gender;
import lombok.Builder;

import java.util.List;

@Builder
public record JobRequestModel(
        String jobName,
        Integer jobTypeId,
        String jobType,
        Integer categoryId,
        String category,
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
        AddressDto jobAddress,

        String provinceCode,
        Integer size,
        Integer page
) {
}
