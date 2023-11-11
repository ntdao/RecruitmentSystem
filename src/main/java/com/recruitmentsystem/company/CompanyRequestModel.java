package com.recruitmentsystem.company;

public record CompanyRequestModel(
        String companyShortName,
        String companyFullName,
        String companyAddress,
        String companyLogo,
        String companyIntroduction,
        String companyImage,
        String companyUrl,
        String industry,
        Integer companyFoundedYear,
        String companyMST,
        String companyLicense,
        Integer companySize,
        String companyTag,
//        boolean isActive,
        String email,
        String phoneNumber,
        String website,
        String facebookUrl,
        String youtubeUrl,
        String linkedinUrl
) {
}
