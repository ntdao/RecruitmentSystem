package com.recruitmentsystem.dto;

import lombok.Builder;

@Builder
public record AddressDto(
        Integer id,
        String provinceCode,
        String province,
        String districtCode,
        String wardCode,
        String address,
        String fullAddress
) {
}
