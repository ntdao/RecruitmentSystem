package com.recruitmentsystem.address.province;

import org.springframework.stereotype.Service;

@Service
public class ProvinceMapper {
    public ProvinceResponseModel provinceToResponseModel(Province province) {
        return ProvinceResponseModel
                .builder()
                .code(province.getProvinceCode())
                .fullName(province.getFullName())
                .build();
    }
}
