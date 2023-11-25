//package com.recruitmentsystem.jobstatus;
//
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
//public class JobStatusService {
//    private final JobStatusMapper jobStatusMapper;
//    private final JobStatusRepository jobStatusRepository;
//    public void addJobStatus(JobStatusRequestModel request) {
//        checkDuplicatedJobStatusName(request.nameEN(), request.nameVI());
//        JobStatus jobStatus = jobStatusMapper.jobStatusRequestModelToJobStatus(request);
//        jobStatusRepository.save(jobStatus);
//    }
//    private void checkDuplicatedJobStatusName(String nameEN, String nameVI) {
//        if (jobStatusRepository.existsJobStatusByJobStatusName(nameEN)) {
//            throw new ResourceAlreadyExistsException("JobStatus EN name already taken");
//        }
//        if (jobStatusRepository.existsJobStatusByJobStatusNameVI(nameVI)) {
//            throw new ResourceAlreadyExistsException("JobStatus VI name already taken");
//        }
//    }
//    public void deleteJobStatus(Integer id) {
//        JobStatus jobStatus = findUserResponseModelById(id);
//        jobStatusRepository.delete(jobStatus);
//    }
//    public List<JobStatus> findAll() {
//        return jobStatusRepository.findAll();
//    }
//    public List<JobStatusResponseModel> findAllJobStatusResponseModel() {
//        return jobStatusRepository.findAll()
//                .stream()
//                .map(jobStatusMapper::jobStatusToResponseModel)
//                .collect(Collectors.toList());
//    }
//    public JobStatus findUserResponseModelById(Integer id) {
//        return jobStatusRepository.findUserResponseModelById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("JobStatus with id " + id + " does not exist"));
//    }
//    public JobStatus findIndustryByName(String name) {
//        return jobStatusRepository.findByJobStatusNameVI(name)
//                .orElseThrow(() -> new ResourceNotFoundException("JobStatus with name " + name + " does not exist"));
//    }
//    public JobStatusResponseModel findJobStatusResponseModelById(Integer id) {
//        return jobStatusMapper.jobStatusToResponseModel(findUserResponseModelById(id));
//    }
//    public JobStatusResponseModel findJobStatusResponseModelByName(String name) {
//        return jobStatusMapper.jobStatusToResponseModel(findIndustryByName(name));
//    }
//    @Transactional
//    public void updateJobStatus(Integer id, JobStatusRequestModel request) {
//        JobStatus updateJobStatus = findUserResponseModelById(id);
//        JobStatus jobStatusRequest = jobStatusMapper.jobStatusRequestModelToJobStatus(request);
//        jobStatusRequest.setJobStatusId(id);
//        jobStatusRepository.save(updateJobStatus);
//    }
//}
