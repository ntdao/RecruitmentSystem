package com.recruitmentsystem.address.province;

import com.recruitmentsystem.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProvinceService {
    private final ProvinceMapper provinceMapper;
    private final ProvinceRepository provinceRepository;

    public List<ProvinceResponseModel> findAllProvinces() {
        return provinceRepository.findAll()
                .stream()
                .map(provinceMapper::provinceToResponseModel)
                .collect(Collectors.toList());
    }

    public String findProvinceByWardCode(String code) {
        return provinceRepository.findProvinceByWardCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot find province by ward code " + code));
    }

    public Province findProvinceByProvinceCode(String code) {
        return provinceRepository.findByProvinceCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Province with code " + code + " not found"));
    }
}
