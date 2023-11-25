package com.recruitmentsystem.address.district;

import lombok.Builder;

@Builder
public record DistrictResponseModel(
        String code,
        String fullName
) {
}
