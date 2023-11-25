package com.recruitmentsystem.address.province;

import lombok.Builder;

@Builder
public record ProvinceResponseModel(
        String code,
        String fullName
) {
}
