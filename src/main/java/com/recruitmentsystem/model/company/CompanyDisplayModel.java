package com.recruitmentsystem.model.company;

import lombok.Builder;

import java.time.Instant;

@Builder
public record CompanyDisplayModel(
        Integer id,
        String name,
        String companyAddress,
        String companyLogo,
        String companySlogan,
        String companyMessage,
        String companyImage,
        Instant createdAt,
        Instant updatedAt
) {
}
