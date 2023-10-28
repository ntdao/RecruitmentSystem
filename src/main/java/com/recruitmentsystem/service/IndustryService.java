package com.recruitmentsystem.service;

import com.recruitmentsystem.common.exception.ResourceAlreadyExistsException;
import com.recruitmentsystem.common.exception.ResourceNotFoundException;
import com.recruitmentsystem.entity.Industry;
import com.recruitmentsystem.entity.Role;
import com.recruitmentsystem.mapper.RoleMapper;
import com.recruitmentsystem.model.role.RoleDisplayModel;
import com.recruitmentsystem.model.role.RoleRequestModel;
import com.recruitmentsystem.repository.IIndustryRepository;
import com.recruitmentsystem.repository.IRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IndustryService {
    private final IIndustryRepository industryRepository;

    public Industry findByName(String name) {
        return industryRepository.findByIndustryNameVn(name)
                .filter(industry -> !(industry.isDeleteFlag()))
                .orElseThrow(() -> new ResourceNotFoundException("Industry with name " + name + " does not exist"));
    }
}
