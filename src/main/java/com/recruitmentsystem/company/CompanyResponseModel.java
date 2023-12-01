package com.recruitmentsystem.company;

import com.recruitmentsystem.address.address.AddressResponseModel;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CompanyResponseModel(
        Integer id,
        String companyShortName,
        String companyFullName,
        AddressResponseModel companyAddress,
        String companyUrl,
        String companyLogo,
        String companyIntroduction,
        Integer industry,
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
