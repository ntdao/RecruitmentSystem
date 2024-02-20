package com.recruitmentsystem.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CompanyDto (
        Integer companyId,
        Integer accountId,
        String password,
        String companyShortName,
        String companyFullName,
        AddressDto companyAddress,
        String companyLogo,
        String companyIntroduction,
        Integer companyIndustryId,
        IndustryDTO companyIndustry,
        String companyImage,
        Integer companyFoundedYear,
        String companyMST,
        String companyLicense,
        String companySize,
        String email,
        String phoneNumber,
        String website,
        String facebookUrl,
        String youtubeUrl,
        String linkedinUrl,
        String companyBranch,
        LocalDateTime createDate,
        LocalDateTime lastModified
) {
}
