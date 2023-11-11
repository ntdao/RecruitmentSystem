package com.recruitmentsystem.address;

import com.recruitmentsystem.address.District.DistrictResponseModel;
import com.recruitmentsystem.address.Province.ProvinceResponseModel;
import com.recruitmentsystem.address.ward.WardResponseModel;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/address")
@RequiredArgsConstructor
public class AddressController {
    private final AddressService addressService;
    @GetMapping("/provinces")
    public List<ProvinceResponseModel> getAllProvinces() {
        return addressService.getAllProvinces();
    }

    @GetMapping("/districts/province")
    public List<DistrictResponseModel> getAllDistrictsByProvince(@RequestParam("code") String code) {
        return addressService.getAllDistrictsByProvince(code);
    }
    @GetMapping("/wards/district")
    public List<WardResponseModel> getAllWardsByDistrict(@RequestParam("code") String code) {
        return addressService.getAllWardsByDistrict(code);
    }
}
