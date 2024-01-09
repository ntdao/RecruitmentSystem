package com.recruitmentsystem.dto;

import lombok.Builder;

@Builder
public record CandidateEducationDto(
        String schoolName,
        String major,
        String description,
        String startDate,
        String endDate,
        String degreeName
) {
}
