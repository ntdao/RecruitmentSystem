package com.recruitmentsystem.dto;

import lombok.Builder;

@Builder
public record DistrictDto(
        String districtCode,
        String fullName
) {
}
