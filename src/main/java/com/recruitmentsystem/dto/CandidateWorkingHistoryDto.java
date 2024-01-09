package com.recruitmentsystem.dto;

import lombok.Builder;

@Builder
public record CandidateWorkingHistoryDto(
        String id,
        String companyName,
        String jobName,
        String description,
        String startDate,
        String endDate
) {
}
