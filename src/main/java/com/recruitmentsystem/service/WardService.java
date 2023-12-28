package com.recruitmentsystem.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recruitmentsystem.dto.WardDto;
import com.recruitmentsystem.entity.Ward;
import com.recruitmentsystem.exception.ResourceNotFoundException;
import com.recruitmentsystem.repository.WardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WardService {
    private final ObjectMapper objectMapper;
    private final WardRepository wardRepository;
    public Ward findWardByWardCode(String code) {
        return wardRepository.findByWardCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Ward with code " + code + " not found"));
    }

    public List<WardDto> getAllWardsByDistrict(String code) {
        return wardRepository.findAllByDistrictCode(code);
//                .stream()
//                .map(w -> objectMapper.convertValue(w, WardDto.class))
//                .collect(Collectors.toList());
    }
}
