package com.recruitmentsystem.address.district;

import org.springframework.stereotype.Service;

@Service
public class DistrictMapper {
    public DistrictResponseModel districtToResponseModel(District district) {
        return DistrictResponseModel
                .builder()
                .code(district.getDistrictCode())
                .fullName(district.getFullName())
                .build();
    }
}
