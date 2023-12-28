package com.recruitmentsystem.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CompanyResponseModel(
        Integer companyId,
        String companyShortName,
        String companyFullName,
        AddressDto companyAddress,
        String companyLogo,
        String companyIntroduction,
        IndustryDto companyIndustry,
        String companyImage,
        Integer companyFoundedYear,
        String companyMST,
        String companyLicense,
        String companySize,
        String companyTag,
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
