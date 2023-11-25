package com.recruitmentsystem.address.district;

import com.recruitmentsystem.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DistrictService {
    private final DistrictMapper districtMapper;
    private final DistrictRepository districtRepository;

    public List<DistrictResponseModel> findAllDistrictsByProvince(String code) {
        return districtRepository.findDistrictByProvinceCode(code)
                .stream()
                .map(districtMapper::districtToResponseModel)
                .collect(Collectors.toList());
    }

    public String findDistrictByWardCode(String code) {
        return districtRepository.findDistrictByWardCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot find district by ward code " + code));
    }

    public District findDistrictByDistrictCode(String code) {
        return districtRepository.findByDistrictCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("District with code " + code + " not found"));
    }
}
