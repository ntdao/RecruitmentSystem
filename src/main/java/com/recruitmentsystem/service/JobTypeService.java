package com.recruitmentsystem.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recruitmentsystem.dto.JobTypeDto;
import com.recruitmentsystem.entity.JobType;
import com.recruitmentsystem.exception.ResourceAlreadyExistsException;
import com.recruitmentsystem.exception.ResourceNotFoundException;
import com.recruitmentsystem.repository.JobTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobTypeService {
    private final ObjectMapper objectMapper;
    private final JobTypeRepository jobTypeRepository;

    public void addJobType(JobTypeDto request) {
        checkDuplicatedJobTypeName(request.jobTypeName(), request.jobTypeNameVI());
        JobType jobType = objectMapper.convertValue(request, JobType.class);
        jobTypeRepository.save(jobType);
    }

    private void checkDuplicatedJobTypeName(String nameEN, String nameVI) {
        if (jobTypeRepository.existsJobTypeByJobTypeName(nameEN)) {
            throw new ResourceAlreadyExistsException("JobType EN name already taken");
        }
        if (jobTypeRepository.existsJobTypeByJobTypeNameVI(nameVI)) {
            throw new ResourceAlreadyExistsException("JobType VI name already taken");
        }
    }

    public void deleteJobType(Integer id) {
        JobType jobType = findById(id);
        jobTypeRepository.delete(jobType);
    }

    public List<JobType> findAll() {
        return jobTypeRepository.findAll();
    }

    public List<JobTypeDto> findAllJobTypeResponseModel() {
        return jobTypeRepository.findAll()
                .stream()
                .map(j -> objectMapper.convertValue(j, JobTypeDto.class))
                .collect(Collectors.toList());
    }

    public JobType findById(Integer id) {
        return jobTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("JobType with id " + id + " does not exist"));
    }

    public JobType findByName(String name) {
        return jobTypeRepository.findByJobTypeNameVI(name)
                .orElseThrow(() -> new ResourceNotFoundException("JobType with name " + name + " does not exist"));
    }

    public JobTypeDto findJobTypeResponseModelById(Integer id) {
        return objectMapper.convertValue(findById(id), JobTypeDto.class);
    }

    public JobTypeDto findJobTypeResponseModelByName(String name) {
        return objectMapper.convertValue(findByName(name), JobTypeDto.class);
    }

    @Transactional
    public void updateJobType(Integer id, JobTypeDto request) {
        JobType updateJobType = findById(id);
        JobType jobTypeRequest = objectMapper.convertValue(request, JobType.class);
        jobTypeRequest.setJobTypeId(id);
        jobTypeRepository.save(updateJobType);
    }
}
