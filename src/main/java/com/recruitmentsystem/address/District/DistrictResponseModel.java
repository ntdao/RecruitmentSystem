package com.recruitmentsystem.address.District;

import lombok.Builder;

@Builder
public record DistrictResponseModel(
        String code,
        String fullName
){
}
