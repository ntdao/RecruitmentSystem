package com.recruitmentsystem.company;

import lombok.Builder;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Builder
public record CompanyResponseModel(
        Integer id,
        String companyShortName,
        String companyFullName,
        String companyAddress,
        String companyUrl,
        String companyLogo,
        String companyIntroduction,
        String industry,
        String companyImage,
        Integer companyFoundedYear,
        String companyMST,
        String companyLicense,
        String companySize,
        String companyTag,
        boolean isActive,
        String email,
        String phoneNumber,
        String website,
        String facebookUrl,
        String youtubeUrl,
        String linkedinUrl,
        List<String> branches,
        LocalDateTime createDate,
        LocalDateTime lastModified
) {
}
