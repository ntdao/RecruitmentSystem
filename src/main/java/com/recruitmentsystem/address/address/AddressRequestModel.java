package com.recruitmentsystem.address.address;

public record AddressRequestModel(
        String provinceCode,
        String districtCode,
        String wardCode,
        String address
) {
}
