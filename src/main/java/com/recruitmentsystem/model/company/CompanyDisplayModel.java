package com.recruitmentsystem.model.company;

import com.recruitmentsystem.entity.CompanyBranch;
import com.recruitmentsystem.entity.Industry;
import lombok.Builder;

import java.time.Instant;
import java.util.List;

@Builder
public record CompanyDisplayModel(
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
        Integer companySize,
        String companyTag,
        boolean isActive,
        String email,
        String phoneNumber,
        String website,
        String facebookUrl,
        String youtubeUrl,
        String linkedinUrl,
        List<String> branches,
        Instant createdAt,
        Instant updatedAt
) {
}
