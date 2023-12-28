package com.recruitmentsystem.dto;

import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Builder
public record UserResponseModel(
        Integer id,
        String email,
        String fullName,
        String phoneNumber,
        AddressDto address,
        String gender,
        LocalDate birthday,
        String imgUrl,
        LocalDateTime createDate,
        LocalDateTime lastModified,
        String roleName,
        List<UserEducationDto> education,
        List<UserWorkingHistoryDto> workingHistory,
        List<SkillDto> skill,
        String desiredJob,
        String educationLevel,
        CategoryDto category,
        String cvUrl
) {
}
