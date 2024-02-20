package com.recruitmentsystem.service;

import com.recruitmentsystem.entity.Ward;
import com.recruitmentsystem.exception.ResourceNotFoundException;
import com.recruitmentsystem.repository.WardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class WardService {
    private final WardRepository wardRepository;

    public Ward findWardByWardCode(String code) {
        return wardRepository.findByWardCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Ward with code " + code + " not found"));
    }

    public List<Map<String, Object>> getAllWardsByDistrict(String code) {
        return wardRepository.findAllByDistrictCode(code);
    }
}
