package com.recruitmentsystem.jobposition;

import com.recruitmentsystem.common.exception.ResourceAlreadyExistsException;
import com.recruitmentsystem.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobPositionService {
    private final JobPositionMapper jobPositionMapper;
    private final JobPositionRepository jobPositionRepository;
    public void addJobPosition(JobPositionRequestModel request) {
        checkDuplicatedJobPositionName(request.nameEN(), request.nameVI());
        JobPosition jobPosition = jobPositionMapper.jobPositionRequestModelToJobPosition(request);
        jobPositionRepository.save(jobPosition);
    }
    private void checkDuplicatedJobPositionName(String nameEN, String nameVI) {
        if (jobPositionRepository.existsJobPositionByJobPositionName(nameEN)) {
            throw new ResourceAlreadyExistsException("JobPosition EN name already taken");
        }
        if (jobPositionRepository.existsJobPositionByJobPositionNameVI(nameVI)) {
            throw new ResourceAlreadyExistsException("JobPosition VI name already taken");
        }
    }
    public void deleteJobPosition(Integer id) {
        JobPosition jobPosition = findById(id);
        jobPositionRepository.delete(jobPosition);
    }
    public List<JobPosition> findAll() {
        return jobPositionRepository.findAll();
    }
    public List<JobPositionResponseModel> findAllJobPositionResponseModel() {
        return jobPositionRepository.findAll()
                .stream()
                .map(jobPositionMapper::jobPositionToResponseModel)
                .collect(Collectors.toList());
    }
    public JobPosition findById(Integer id) {
        return jobPositionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("JobPosition with id " + id + " does not exist"));
    }
    public JobPosition findByName(String name) {
        return jobPositionRepository.findByJobPositionNameVI(name)
                .orElseThrow(() -> new ResourceNotFoundException("JobPosition with name " + name + " does not exist"));
    }
    public JobPositionResponseModel findJobPositionResponseModelById(Integer id) {
        return jobPositionMapper.jobPositionToResponseModel(findById(id));
    }
    public JobPositionResponseModel findJobPositionResponseModelByName(String name) {
        return jobPositionMapper.jobPositionToResponseModel(findByName(name));
    }
    @Transactional
    public void updateJobPosition(Integer id, JobPositionRequestModel request) {
        JobPosition updateJobPosition = findById(id);
        JobPosition jobPositionRequest = jobPositionMapper.jobPositionRequestModelToJobPosition(request);
        jobPositionRequest.setJobPositionId(id);
        jobPositionRepository.save(updateJobPosition);
    }
}
