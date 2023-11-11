package com.recruitmentsystem.jobtype;

import com.recruitmentsystem.common.exception.ResourceAlreadyExistsException;
import com.recruitmentsystem.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobTypeService {
    private final JobTypeMapper jobTypeMapper;
    private final JobTypeRepository jobTypeRepository;
    public void addJobType(JobTypeRequestModel request) {
        checkDuplicatedJobTypeName(request.nameEN(), request.nameVI());
        JobType jobType = jobTypeMapper.jobTypeRequestModelToJobType(request);
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
    public List<JobTypeResponseModel> findAllJobTypeResponseModel() {
        return jobTypeRepository.findAll()
                .stream()
                .map(jobTypeMapper::jobTypeToResponseModel)
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
    public JobTypeResponseModel findJobTypeResponseModelById(Integer id) {
        return jobTypeMapper.jobTypeToResponseModel(findById(id));
    }
    public JobTypeResponseModel findJobTypeResponseModelByName(String name) {
        return jobTypeMapper.jobTypeToResponseModel(findByName(name));
    }
    @Transactional
    public void updateJobType(Integer id, JobTypeRequestModel request) {
        JobType updateJobType = findById(id);
        JobType jobTypeRequest = jobTypeMapper.jobTypeRequestModelToJobType(request);
        jobTypeRequest.setJobTypeId(id);
        jobTypeRepository.save(updateJobType);
    }
}
