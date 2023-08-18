package com.recruitmentsystem.model.company;

import lombok.Builder;

import java.time.Instant;

@Builder
public record CompanyDisplayModel(
        String companyName,
        String companyAddress,
        String companyUrl,
        String companyLogo,
        String companySlogan,
        String companyIntroduction,
        String companyField,
        String companyImage,
        Instant createdAt,
        Instant updatedAt
//        boolean isActive
) {
}
