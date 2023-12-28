package com.recruitmentsystem.dto;

public record CompanyRequestModel (
        String password,
        String roleName,
        String companyShortName,
        String companyFullName,
        AddressDto companyAddress,
        String companyLogo,
        String companyIntroduction,
        String companyImage,
        String companyIndustry,
        String companyFoundedYear,
        String companyMST,
        String companyLicense,
        String companySize,
        String companyBranch,
        String email,
        String phoneNumber,
        String website,
        String facebookUrl,
        String youtubeUrl,
        String linkedinUrl
) {
}
