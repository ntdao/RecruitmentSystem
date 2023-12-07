package com.recruitmentsystem.address.address;

import com.recruitmentsystem.address.district.DistrictService;
import com.recruitmentsystem.address.province.Province;
import com.recruitmentsystem.address.province.ProvinceService;
import com.recruitmentsystem.address.ward.Ward;
import com.recruitmentsystem.address.ward.WardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressMapper {
    private final DistrictService districtService;
    private final ProvinceService provinceService;
    private final WardService wardService;

    public AddressResponseModel addressToResponseModel(Address address) {
        // kiem tra address
        if (address == null || address.getWard() == null) {
            return null;
        }
        String wardCode = address.getWard().getWardCode();
        Province province = provinceService.findProvinceByWardCode(wardCode);
        return AddressResponseModel
                .builder()
                .id(address.getAddressId())
                .provinceCode(province.getProvinceCode())
                .province(province.getName())
                .districtCode(districtService.findDistrictByWardCode(wardCode))
                .wardCode(wardCode)
                .address(address.getAddress())
                .fullAddress(address.getFullAddress())
                .build();
    }

    public Address addressRequestModelToAddress(AddressRequestModel request) {
        Ward ward = wardService.findWardByWardCode(request.wardCode());
        String fullAddress = request.address() + ", "
                + ward.getFullName() + ", "
                + districtService.findDistrictByDistrictCode(request.districtCode()).getFullName() + ", "
                + provinceService.findProvinceByProvinceCode(request.provinceCode()).getFullName();
        return Address
                .builder()
                .ward(ward)
                .address(request.address())
                .fullAddress(fullAddress)
                .build();
    }
}
