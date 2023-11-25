package com.recruitmentsystem.address.address;

import com.recruitmentsystem.address.district.DistrictResponseModel;
import com.recruitmentsystem.address.district.DistrictService;
import com.recruitmentsystem.address.province.ProvinceResponseModel;
import com.recruitmentsystem.address.province.ProvinceService;
import com.recruitmentsystem.address.ward.WardResponseModel;
import com.recruitmentsystem.address.ward.WardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/address")
@RequiredArgsConstructor
public class AddressController {
    private final AddressService addressService;
    private final DistrictService districtService;
    private final ProvinceService provinceService;
    private final WardService wardService;

    @GetMapping("/provinces")
    public List<ProvinceResponseModel> getAllProvinces() {
        return provinceService.findAllProvinces();
    }

    @GetMapping("/districts/province")
    public List<DistrictResponseModel> getAllDistrictsByProvince(@RequestParam("code") String code) {
        return districtService.findAllDistrictsByProvince(code);
    }

    @GetMapping("/wards/district")
    public List<WardResponseModel> getAllWardsByDistrict(@RequestParam("code") String code) {
        return wardService.getAllWardsByDistrict(code);
    }

    @GetMapping("/company")
    public AddressResponseModel getCompanyAddress(@RequestParam("id") Integer id) {
        return addressService.getCompanyAddressResponseModel(id);
    }

    // test address request to entity
    @PostMapping("/company")
    public void updateCompanyAddress(@RequestBody AddressRequestModel request) {
        addressService.addressRequestModelToEntity(request);
    }
}
