package com.recruitmentsystem.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recruitmentsystem.dto.ProvinceDto;
import com.recruitmentsystem.entity.Province;
import com.recruitmentsystem.exception.ResourceNotFoundException;
import com.recruitmentsystem.repository.ProvinceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProvinceService {
    private final ProvinceRepository provinceRepository;

    public List<ProvinceDto> findAllProvinces() {
        return provinceRepository.findAllProvince();
    }

    public Province findProvinceByWardCode(String code) {
        return provinceRepository.findProvinceByWardCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot find province by ward code " + code));
    }

    public String findProvinceByProvinceCode(String code) {
        return provinceRepository.findByProvinceCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Province with code " + code + " not found"));
    }
}
