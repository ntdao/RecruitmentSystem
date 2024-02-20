package com.recruitmentsystem.controller;

import com.recruitmentsystem.service.DistrictService;
import com.recruitmentsystem.service.ProvinceService;
import com.recruitmentsystem.service.WardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/address")
@RequiredArgsConstructor
public class AddressController {
    private final DistrictService districtService;
    private final ProvinceService provinceService;
    private final WardService wardService;

    @GetMapping("/provinces")
    public List<Map<String, Object>> getAllProvinces() {
        return provinceService.findAllProvinces();
    }

    @GetMapping("/districts/province")
    public List<Map<String, Object>> getAllDistrictsByProvince(@RequestParam("code") String code) {
        return districtService.findAllDistrictsByProvince(code);
    }

    @GetMapping("/wards/district")
    public List<Map<String, Object>> getAllWardsByDistrict(@RequestParam("code") String code) {
        return wardService.getAllWardsByDistrict(code);
    }
}
