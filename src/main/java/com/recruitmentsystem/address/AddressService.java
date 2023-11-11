package com.recruitmentsystem.address;

import com.recruitmentsystem.address.District.DistrictMapper;
import com.recruitmentsystem.address.District.DistrictRepository;
import com.recruitmentsystem.address.District.DistrictResponseModel;
import com.recruitmentsystem.address.Province.ProvinceMapper;
import com.recruitmentsystem.address.Province.ProvinceRepository;
import com.recruitmentsystem.address.Province.ProvinceResponseModel;
import com.recruitmentsystem.address.ward.WardMapper;
import com.recruitmentsystem.address.ward.WardRepository;
import com.recruitmentsystem.address.ward.WardResponseModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final DistrictMapper districtMapper;
    private final DistrictRepository districtRepository;
    private final ProvinceMapper provinceMapper;
    private final ProvinceRepository provinceRepository;
    private final WardMapper wardMapper;
    private final WardRepository wardRepository;

    public List<ProvinceResponseModel> getAllProvinces() {
        return provinceRepository.findAll()
                .stream()
                .map(provinceMapper::provinceToResponseModel)
                .collect(Collectors.toList());
    }

    public List<DistrictResponseModel> getAllDistrictsByProvince(String code) {
        return districtRepository.findAllByProvinceCode(code)
                .stream()
                .map(districtMapper::districtToResponseModel)
                .collect(Collectors.toList());
    }

    public List<WardResponseModel> getAllWardsByDistrict(String code) {
        return wardRepository.findAllByDistrictCode(code)
                .stream()
                .map(wardMapper::wardToResponseModel)
                .collect(Collectors.toList());
    }
}
