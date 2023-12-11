package com.recruitmentsystem.address.address;

import com.recruitmentsystem.address.district.DistrictService;
import com.recruitmentsystem.address.province.Province;
import com.recruitmentsystem.address.province.ProvinceService;
import com.recruitmentsystem.address.ward.Ward;
import com.recruitmentsystem.address.ward.WardService;
import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressMapper {
    private final AddressRepository addressRepository;
    private final DistrictService districtService;
    private final ProvinceService provinceService;
    private final WardService wardService;

    public AddressResponseModel addressToResponseModel(Address address) {
        // kiem tra address
        if (address == null || address.getWard() == null) {
            return null;
        }
        String wardCode = address.getWard().getWardCode();
        Tuple tuple = addressRepository.getProvinceDistrictByWard(wardCode);
        return AddressResponseModel
                .builder()
                .id(address.getAddressId())
                .provinceCode(tuple.get("provinceCode", String.class))
                .province(tuple.get("provinceName", String.class))
                .districtCode(tuple.get("districtCode", String.class))
                .wardCode(wardCode)
                .address(address.getAddress())
                .fullAddress(address.getFullAddress())
                .build();
    }

    public Address addressRequestModelToAddress(AddressRequestModel request) {
        Ward ward = wardService.findWardByWardCode(request.wardCode());
        String fullAddress = request.address() + ", "
                + ward.getFullName() + ", "
                + districtService.findDistrictByDistrictCode(request.districtCode()) + ", "
                + provinceService.findProvinceByProvinceCode(request.provinceCode());
        return Address
                .builder()
                .ward(ward)
                .address(request.address())
                .fullAddress(fullAddress)
                .build();
    }
}
