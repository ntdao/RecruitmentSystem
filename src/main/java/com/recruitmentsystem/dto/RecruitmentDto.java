package com.recruitmentsystem.dto;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record RecruitmentDto(
        Integer applicationId,
        String candidateFullName,
        String candidateImgUrl,
        LocalDate candidateBirthday,
        String candidateAddress,
        String jobName,
        String companyName,
        String companyLogo,
        String jobQuantity,
        String jobCandidate,
        String applicationTimeAgo,
        Integer applicationStatus
) {
}
