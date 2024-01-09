package com.recruitmentsystem.service;

import com.recruitmentsystem.entity.Degree;
import com.recruitmentsystem.exception.ResourceNotFoundException;
import com.recruitmentsystem.repository.DegreeRepository;
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

    public Degree findDegreeByName(String name) {
        return degreeRepository.findByDegreeName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Degree with name " + name + " not found"));
    }

    public List<Degree> findByName(String name) {
        return degreeRepository.findByDegreeNameVIContains(name);
    }
}
