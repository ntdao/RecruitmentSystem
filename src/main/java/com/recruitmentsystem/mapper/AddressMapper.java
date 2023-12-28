package com.recruitmentsystem.mapper;

import com.recruitmentsystem.service.DistrictService;
import com.recruitmentsystem.service.ProvinceService;
import com.recruitmentsystem.dto.AddressDto;
import com.recruitmentsystem.entity.Address;
import com.recruitmentsystem.entity.Ward;
import com.recruitmentsystem.service.WardService;
import com.recruitmentsystem.repository.AddressRepository;
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

    public AddressDto addressToResponseModel(Address address) {
        if (address == null) {
            return null;
        }
        String wardCode = address.getWard().getWardCode();
        Tuple tuple = addressRepository.getProvinceDistrictByWard(wardCode);
        return AddressDto
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

    public Address addressRequestModelToAddress(AddressDto request) {
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
