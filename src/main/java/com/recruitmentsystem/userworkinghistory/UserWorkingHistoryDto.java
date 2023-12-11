package com.recruitmentsystem.userworkinghistory;

import lombok.Builder;

@Builder
public record UserWorkingHistoryDto(
        String id,
        String companyName,
        String jobName,
        String description,
        String startDate,
        String endDate
) {
}
