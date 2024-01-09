package com.recruitmentsystem.dto;

import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Builder
public record CandidateResponseModel(
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
        List<CandidateEducationDto> education,
        List<CandidateWorkingHistoryDto> workingHistory,
        List<SkillDto> skill,
        String desiredJob,
        String educationLevel,
        CategoryDto category,
        String cvUrl
) {
}
