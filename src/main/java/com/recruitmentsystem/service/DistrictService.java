package com.recruitmentsystem.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recruitmentsystem.dto.DistrictDto;
import com.recruitmentsystem.exception.ResourceNotFoundException;
import com.recruitmentsystem.repository.DistrictRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DistrictService {
    private final DistrictRepository districtRepository;
    private final ObjectMapper objectMapper;

    public List<DistrictDto> findAllDistrictsByProvince(String code) {
        return districtRepository.findDistrictByProvinceCode(code);
//                .stream()
//                .map(d -> objectMapper.convertValue(d, DistrictDto.class))
//                .collect(Collectors.toList());
    }

    public String findDistrictByWardCode(String code) {
        return districtRepository.findDistrictByWardCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot find district by ward code " + code));
    }

    public String findDistrictByDistrictCode(String code) {
        return districtRepository.findByDistrictCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("District with code " + code + " not found"));
    }
}
