package com.recruitmentsystem.controller;

import com.recruitmentsystem.dto.*;
import com.recruitmentsystem.service.DistrictService;
import com.recruitmentsystem.service.ProvinceService;
import com.recruitmentsystem.service.WardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/address")
@RequiredArgsConstructor
public class AddressController {
    private final DistrictService districtService;
    private final ProvinceService provinceService;
    private final WardService wardService;

    @GetMapping("/provinces")
    public List<ProvinceDto> getAllProvinces() {
        return provinceService.findAllProvinces();
    }

    @GetMapping("/districts/province")
    public List<DistrictDto> getAllDistrictsByProvince(@RequestParam("code") String code) {
        return districtService.findAllDistrictsByProvince(code);
    }

    @GetMapping("/wards/district")
    public List<WardDto> getAllWardsByDistrict(@RequestParam("code") String code) {
        return wardService.getAllWardsByDistrict(code);
    }
}
