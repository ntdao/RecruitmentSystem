package com.recruitmentsystem.dto;

import lombok.Builder;

@Builder
public record UserEducationDto(
        String schoolName,
        String major,
        String description,
        String startDate,
        String endDate,
        String degreeName
) {
}
