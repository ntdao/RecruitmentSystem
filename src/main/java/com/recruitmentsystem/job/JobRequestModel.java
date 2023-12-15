package com.recruitmentsystem.job;

import com.recruitmentsystem.address.address.AddressRequestModel;
import com.recruitmentsystem.common.enums.Gender;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record JobRequestModel(
        String name,
        List<AddressRequestModel> jobAddresses,
//        Integer jobPosition,
        String jobDescription,
        String jobRequirement,
        Integer salaryMax,
        Integer salaryMin,
        boolean isSalaryVisible,
        LocalDateTime jobExpiredDate,
        String jobUrl,
//        String jobBenefit,
        Integer jobQuantity,
        Integer jobType,
//        String jobTag,
        Gender jobGender,
        String jobExperience,
        Integer categoryId,
        String minEducationLevel
) {
}
