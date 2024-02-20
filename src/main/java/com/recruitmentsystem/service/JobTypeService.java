package com.recruitmentsystem.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recruitmentsystem.dto.JobTypeDTO;
import com.recruitmentsystem.entity.JobType;
import com.recruitmentsystem.exception.ResourceAlreadyExistsException;
import com.recruitmentsystem.exception.ResourceNotFoundException;
import com.recruitmentsystem.repository.JobTypeRepository;
import com.recruitmentsystem.utils.DataFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
public class JobTypeService {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private JobTypeRepository jobTypeRepository;

    public void save(JobTypeDTO jobType) {
        if (Objects.isNull(jobType.getId())) {
            create(jobType);
        } else {
            update(jobType);
        }
    }

    public void create(JobTypeDTO jobType) {
        jobTypeRepository.save(checkValid(jobType));
    }

    private JobType checkValid(JobTypeDTO dto) {
        if (jobTypeRepository.countByNameAndCode(
                dto.getId(),
                DataFormat.lower(dto.getName()),
                DataFormat.lower(dto.getCode())) > 0) {
            throw new ResourceAlreadyExistsException("Tên loại hình công việc/mã loại hình công việc đã tồn tại");
        }
        return objectMapper.convertValue(dto, JobType.class);
    }

    public void update(JobTypeDTO _jobType) {
        Optional<JobType> op = jobTypeRepository.findById(_jobType.getId());
        JobType jobType = checkValid(_jobType);
        if (op.isPresent()) {
            JobType r = op.get();
            r.setName(jobType.getName());
            r.setCode(jobType.getCode());
            jobTypeRepository.save(r);
        } else {
            throw new ResourceNotFoundException("Không tìm thấy loại hình công việc!");
        }
    }

    public Page<JobTypeDTO> findAll(JobTypeDTO jobType) {
        Pageable pageable = PageRequest.of(jobType.getPage() - 1, jobType.getSize());
        return jobTypeRepository.findAll(DataFormat.lower(jobType.getName()), pageable)
                .map(r -> objectMapper.convertValue(r, JobTypeDTO.class));
    }

    public JobTypeDTO findDTOById(Integer id) {
        Optional<JobType> op = jobTypeRepository.findById(id);
        if (op.isPresent()) {
            return objectMapper.convertValue(op.get(), JobTypeDTO.class);
        } else {
            throw new ResourceNotFoundException("Không tìm thấy loại hình công việc!");
        }
    }

    public JobType findById(Integer id) {
        Optional<JobType> op = jobTypeRepository.findById(id);
        if (op.isPresent()) {
            return op.get();
        } else {
            throw new ResourceNotFoundException("Không tìm thấy loại hình công việc!");
        }
    }

    @Transactional
    public void delete(Integer id) {
        JobType jobType = jobTypeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy loại hình công việc!"));
        jobTypeRepository.delete(jobType);
    }

    public JobType findByName(String name) {
        return jobTypeRepository.findByName(name).get();
    }
}

