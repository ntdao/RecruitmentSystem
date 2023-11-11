package com.recruitmentsystem.address.ward;

import org.springframework.stereotype.Service;

@Service
public class WardMapper {
    public WardResponseModel wardToResponseModel(Ward ward) {
        return WardResponseModel
                .builder()
                .code(ward.getWardCode())
                .fullName(ward.getFullName())
                .build();
    }
}
