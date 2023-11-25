package com.recruitmentsystem.address.ward;

import com.recruitmentsystem.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WardService {

    private final WardMapper wardMapper;
    private final WardRepository wardRepository;
    public Ward findWardByWardCode(String code) {
        return wardRepository.findByWardCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Ward with code " + code + " not found"));
    }

    public List<WardResponseModel> getAllWardsByDistrict(String code) {
        return wardRepository.findAllByDistrictCode(code)
                .stream()
                .map(wardMapper::wardToResponseModel)
                .collect(Collectors.toList());
    }
}
