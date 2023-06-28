package com.recruitmentsystem.model.company;

import lombok.Builder;

import java.time.Instant;

@Builder
public record CompanyDisplayModel(
        Integer id,
        String name,
        String imgUrl,
        Instant createdAt,
        Instant updatedAt
) {
}
