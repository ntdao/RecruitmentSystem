package com.recruitmentsystem.dto;

import lombok.Builder;

@Builder
public record JobTypeDto(
        Integer jobTypeId,
        String jobTypeName,
        String jobTypeNameVI
) {
}
