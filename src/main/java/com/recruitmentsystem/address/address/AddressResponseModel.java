package com.recruitmentsystem.address.address;

import lombok.Builder;

@Builder
public record AddressResponseModel(
        Integer id,
        String provinceCode,
        String districtCode,
        String wardCode,
        String address,
        String fullAddress
) {
}
