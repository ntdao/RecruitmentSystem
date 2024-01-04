package com.recruitmentsystem.dto;

import java.time.LocalDateTime;

public record InterviewDto(
        LocalDateTime interviewTime,
        Integer interviewType,
        String interviewAddress,
        Integer interviewStatus
) {
}
