//package com.recruitmentsystem.service;
//
//import com.recruitmentsystem.common.exception.ResourceAlreadyExistsException;
//import com.recruitmentsystem.common.exception.ResourceNotFoundException;
//import com.recruitmentsystem.entity.JobLevel;
//import com.recruitmentsystem.entity.Role;
//import com.recruitmentsystem.mapper.JobLevelMapper;
//import com.recruitmentsystem.model.jobLevel.JobLevelDisplayModel;
//import com.recruitmentsystem.model.jobLevel.JobLevelRequestModel;
//import com.recruitmentsystem.repository.IJobLevelRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.Instant;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//public class JobLevelService {
//    private final IJobLevelRepository jobLevelRepository;
//    private final JobLevelMapper jobLevelMapper;
//    public void addJobLevel(JobLevelRequestModel request) {
//        String jobLevelName = String.valueOf(request.name());
//        if (jobLevelRepository.existsJobLevelByJobLevelName(jobLevelName)) {
//            throw new ResourceAlreadyExistsException("Job level name already taken");
//        }
//
//        // add
//        JobLevel jobLevel = jobLevelMapper.jobLevelRequestModelToJobLevel(request);
//        jobLevel.setCreatedAt(Instant.now());
//        jobLevelRepository.save(jobLevel);
//    }
//
//    public List<JobLevelDisplayModel> findAllJobLevels() {
//        List<JobLevel> jobLevels = jobLevelRepository.findAll();
//        return jobLevels.stream()
//                .filter(jobLevel -> !jobLevel.isDeleteFlag())
//                .map(jobLevelMapper::jobLevelToDisplayModel)
//                .collect(Collectors.toList());
//    }
//
//    public JobLevelDisplayModel findJobLevelById(Integer id) {
//        return jobLevelRepository.findById(id)
//                .filter(jobLevel -> !jobLevel.isDeleteFlag())
//                .map(jobLevelMapper::jobLevelToDisplayModel)
//                .orElseThrow(() -> new ResourceNotFoundException("Job level with id " + id + " does not exist"));
//    }
//    public JobLevel findById(Integer id) {
//        return jobLevelRepository.findById(id)
//                .filter(jobLevel -> !jobLevel.isDeleteFlag())
//                .orElseThrow(() -> new ResourceNotFoundException("Job level with id " + id + " does not exist"));
//    }
//
//    public List<JobLevelDisplayModel> findByJobLevelName(String name) {
//        return jobLevelRepository.findAll()
//                .stream()
//                .filter(l -> (!l.isDeleteFlag() &&
//                        l.getJobLevelName().contains(name)))
//                .map(jobLevelMapper::jobLevelToDisplayModel)
//                .collect(Collectors.toList());
////        return jobLevelRepository.findByJobLevelName(name)
////                .filter(jobLevel -> !(jobLevel.isDeleteFlag()))
////                .orElseThrow(() -> new ResourceNotFoundException("Job level with name " + name + " does not exist"));
//    }
//
//    public JobLevel findJobLevelByName(String name) {
//        return jobLevelRepository.findByJobLevelName(name)
//                .filter(jobLevel -> !(jobLevel.isDeleteFlag()))
//                .orElseThrow(() -> new ResourceNotFoundException("Job level with name " + name + " does not exist"));
//    }
//
//    @Transactional
//    public void updateJobLevel(Integer id, JobLevel request) {
//        // tim jobLevel theo id
//        JobLevel updateJobLevel = findById(id);
////        updateJobLevel.setUpdatedAt(LocalDateTime.now());
//
//        // tao ban ghi luu thong tin cu cua jobLevel
//        JobLevel oldJobLevel = new JobLevel(updateJobLevel, id, true);
//        jobLevelRepository.save(oldJobLevel);
//
//        // update jobLevel
//        updateJobLevel = request;
//        updateJobLevel.setJobLevelId(id);
//        updateJobLevel.setCreatedAt(oldJobLevel.getCreatedAt());
//        updateJobLevel.setCreatedBy(oldJobLevel.getCreatedBy());
//        updateJobLevel.setUpdatedAt(oldJobLevel.getUpdatedAt());
////        updateJobLevel.setUpdatedBy();
//        jobLevelRepository.save(updateJobLevel);
//    }
//
//    public void deleteJobLevel(Integer id) {
//        JobLevel jobLevel = findById(id);
//        jobLevel.setDeleteFlag(true);
//        jobLevelRepository.save(jobLevel);
//    }
//}
