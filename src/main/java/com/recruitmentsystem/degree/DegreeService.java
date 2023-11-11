package com.recruitmentsystem.degree;

import com.recruitmentsystem.common.exception.ResourceNotFoundException;
import com.recruitmentsystem.degree.Degree;
import com.recruitmentsystem.degree.DegreeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DegreeService {
    private final DegreeRepository degreeRepository;
    public List<Degree> findAll() {
        return degreeRepository.findAll();
    }
    public Degree findById(Integer id) {
        return degreeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Degree with id " + id + " not found"));
    }
}
