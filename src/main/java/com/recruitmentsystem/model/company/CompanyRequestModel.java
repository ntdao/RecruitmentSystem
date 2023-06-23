package com.recruitmentsystem.model.company;

import java.time.Instant;

public record CompanyRequestModel (
        String name,
        String imgUrl,
        Instant createdAt,
        Instant updatedAt
){
}
