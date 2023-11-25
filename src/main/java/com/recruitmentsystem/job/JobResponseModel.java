package com.recruitmentsystem.job;

import com.recruitmentsystem.address.address.Address;
import com.recruitmentsystem.address.address.AddressResponseModel;
import com.recruitmentsystem.skill.Skill;
import com.recruitmentsystem.skill.SkillRepository;
import com.recruitmentsystem.skill.SkillResponseModel;
import lombok.Builder;
import org.springframework.cglib.core.Local;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Builder
public record JobResponseModel(
        Integer id,
        String name,
        String companyLogo,
        List<AddressResponseModel> jobAddress,
        String jobPosition,
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
        LocalDateTime jobExpiredDate,
        String jobUrl,
        String category,
        List<SkillResponseModel> jobSkill,
        LocalDateTime createdAt,
        String companyName
) {
}
