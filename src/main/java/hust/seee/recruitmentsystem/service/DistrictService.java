package hust.seee.recruitmentsystem.service;

import hust.seee.recruitmentsystem.exception.ResourceNotFoundException;
import hust.seee.recruitmentsystem.repository.DistrictRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DistrictService {
    private final DistrictRepository districtRepository;

    public List<Map<String, Object>> findAllDistrictsByProvince(String code) {
        return districtRepository.findDistrictByProvinceCode(code);
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
