package com.recruitmentsystem.address.Province;

import lombok.Builder;

@Builder
public record ProvinceResponseModel (
        String code,
        String fullName
){
}
