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
//        String jobPosition,
        String jobDescription,
        String jobRequirement,
        String salaryMax,
        String salaryMin,
        String salary,
        String jobExpiredDate,
        String jobUrl,
//        String jobBenefit,
        String jobQuantity,
        String jobType,
//        String jobTag,
        Gender jobGender,
        String jobExperience,
        String categoryId,
        String minEducationLevel
) {
}
