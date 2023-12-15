//package com.recruitmentsystem.jobposition;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.recruitmentsystem.common.exception.ResourceAlreadyExistsException;
//import com.recruitmentsystem.common.exception.ResourceNotFoundException;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//public class JobPositionService {
//    private final ObjectMapper objectMapper;
//    private final JobPositionRepository jobPositionRepository;
//    public void addJobPosition(JobPositionRequestModel request) {
//        checkDuplicatedJobPositionName(request.jobPositionName(), request.jobPositionNameVI());
//        JobPosition jobPosition = objectMapper.convertValue(request, JobPosition.class);
//        jobPositionRepository.save(jobPosition);
//    }
//    private void checkDuplicatedJobPositionName(String nameEN, String nameVI) {
//        if (jobPositionRepository.existsJobPositionByJobPositionName(nameEN)) {
//            throw new ResourceAlreadyExistsException("JobPosition EN name already taken");
//        }
//        if (jobPositionRepository.existsJobPositionByJobPositionNameVI(nameVI)) {
//            throw new ResourceAlreadyExistsException("JobPosition VI name already taken");
//        }
//    }
//    public void deleteJobPosition(Integer id) {
//        JobPosition jobPosition = findById(id);
//        jobPositionRepository.delete(jobPosition);
//    }
//    public List<JobPosition> findAll() {
//        return jobPositionRepository.findAll();
//    }
//    public List<JobPositionResponseModel> findAllJobPositionResponseModel() {
//        return jobPositionRepository.findAll()
//                .stream()
//                .map(j -> objectMapper.convertValue(j, JobPositionResponseModel.class))
//                .collect(Collectors.toList());
//    }
//    public JobPosition findById(Integer id) {
//        return jobPositionRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("JobPosition with id " + id + " does not exist"));
//    }
//    public JobPosition findByName(String name) {
//        return jobPositionRepository.findByJobPositionNameVI(name)
//                .orElseThrow(() -> new ResourceNotFoundException("JobPosition with name " + name + " does not exist"));
//    }
//    public JobPositionResponseModel findJobPositionResponseModelById(Integer id) {
//        return objectMapper.convertValue(findById(id), JobPositionResponseModel.class);
//    }
//    public JobPositionResponseModel findJobPositionResponseModelByName(String name) {
//        return objectMapper.convertValue(findByName(name), JobPositionResponseModel.class);
//    }
//    @Transactional
//    public void updateJobPosition(Integer id, JobPositionRequestModel request) {
//        JobPosition updateJobPosition = findById(id);
//        JobPosition jobPositionRequest = objectMapper.convertValue(request, JobPosition.class);
//        jobPositionRequest.setJobPositionId(id);
//        jobPositionRepository.save(updateJobPosition);
//    }
//}
