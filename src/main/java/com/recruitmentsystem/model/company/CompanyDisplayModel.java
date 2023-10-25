package com.recruitmentsystem.model.company;

import com.recruitmentsystem.entity.CompanyBranch;
import lombok.Builder;

import java.time.Instant;
import java.util.List;

@Builder
public record CompanyDisplayModel(
        Integer companyId,
        String companyName,
        String companyAddress,
        String companyUrl,
        String companyLogo,
        String companySlogan,
        String companyIntroduction,
        String companyField,
        String companyImage,
        List<CompanyBranch> branches,
        Instant createdAt,
        Instant updatedAt
//        boolean isActive
) {
}
