package com.recruitmentsystem.dto;

import lombok.Builder;

@Builder
public record ProvinceDto(
        String provinceCode,
        String fullName
) {
}
