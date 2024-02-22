package hust.seee.recruitmentsystem.service;

import hust.seee.recruitmentsystem.entity.Ward;
import hust.seee.recruitmentsystem.exception.ResourceNotFoundException;
import hust.seee.recruitmentsystem.repository.WardRepository;
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
