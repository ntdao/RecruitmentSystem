package com.recruitmentsystem.usereducation;

import com.recruitmentsystem.degree.Degree;
import com.recruitmentsystem.degree.DegreeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserEducationService {
    private final DegreeRepository degreeRepository;
    public List<Degree> findAll() {
        return degreeRepository.findAll();
    }
}
