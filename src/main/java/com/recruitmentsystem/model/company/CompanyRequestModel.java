package com.recruitmentsystem.model.company;

import java.time.Instant;

public record CompanyRequestModel(
        String name,
        String companyLogo,
        String companyInfo,
        Instant createdAt,
        Instant updatedAt
) {
}
