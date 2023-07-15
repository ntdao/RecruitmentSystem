package com.recruitmentsystem.model.company;

import java.time.Instant;

public record CompanyRequestModel(
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
