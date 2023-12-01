package com.recruitmentsystem.company;

import com.recruitmentsystem.address.address.AddressRequestModel;

public record CompanyRequestModel(
        String password,
        String roleName,
        String companyShortName,
        String companyFullName,
        AddressRequestModel companyAddress,
        String companyLogo,
        String companyIntroduction,
        String companyImage,
        String companyUrl,
        Integer industryId,
        Integer companyFoundedYear,
        String companyMST,
        String companyLicense,
        String companySize,
        String companyTag,
        String companyBranch,
        String email,
        String phoneNumber,
        String website,
        String facebookUrl,
        String youtubeUrl,
        String linkedinUrl
) {
}
