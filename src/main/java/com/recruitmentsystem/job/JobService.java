package com.recruitmentsystem.job;

import com.recruitmentsystem.common.exception.ResourceNotFoundException;
import com.recruitmentsystem.common.myEnum.JobStatus;
import com.recruitmentsystem.company.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobService {
    private final JobRepository jobRepository;
    private final JobMapper jobMapper;
    private final CompanyService companyService;

    public void addJob(JobRequestModel requestModel, Principal connectedUser) {
        Job job = jobMapper.jobRequestModelToJob(requestModel);
        jobRepository.save(job);
    }

    public List<JobResponseModel> findAllJobs() {
        return jobRepository.findAllJobs()
                .stream()
                .filter(j -> j.getJobStatus() == JobStatus.RECRUITING)
                .map(jobMapper::jobToResponseModel)
                .collect(Collectors.toList());
    }

    public List<JobResponseModel> findAllJobsByAdmin() {
        List<Job> jobs = jobRepository.findAllJobs();
        return jobs.stream()
                .map(jobMapper::jobToResponseModel).collect(Collectors.toList());
    }

    public List<JobResponseModel> findAllJobsByCompanyId(Integer companyId) {
        companyService.existsById(companyId);
        return jobRepository.findAllJobByCompany(companyId)
                .stream()
                .filter(j -> j.getJobStatus() == JobStatus.RECRUITING)
                .map(jobMapper::jobToResponseModel).collect(Collectors.toList());
    }

    public List<JobResponseModel> findAllByName(String name) {
        List<Job> jobs = jobRepository.findAll();
        return jobs.stream().filter(job -> (!job.isDeleteFlag() && job.getJobName().contains(name))).map(jobMapper::jobToResponseModel).collect(Collectors.toList());
    }

    public JobResponseModel findById(Integer id) {
        return jobMapper.jobToResponseModel(findJobById(id));
    }

    public Job findJobById(Integer id) {
        return jobRepository.findByJobIdAndDeleteFlagFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job with id " + id + " does not exist"));
    }

    public List<Job> findJobByJobNameAdmin(String name) {
        return jobRepository.findByJobName(name).stream().filter(j -> !j.isDeleteFlag()).collect(Collectors.toList());
    }

    public List<JobResponseModel> findJobByJobName(String name) {
        return jobRepository.findByJobName(name).stream().filter(j -> !j.isDeleteFlag()).map(jobMapper::jobToResponseModel).collect(Collectors.toList());
    }

    @Transactional
    public void updateJob(Integer id, JobRequestModel requestModel, Principal connectedUser) {

        Job updateJob = findJobById(id);
        Job oldJob = new Job(id, updateJob, true);
        jobRepository.save(oldJob);

        updateJob = jobMapper.jobRequestModelToJob(requestModel);
        updateJob.setJobId(id);
//        updateJob.setCreatedAt(oldJob.getCreatedAt());
//        updateJob.setCreatedBy(oldJob.getCreatedBy());
//        updateJob.setUpdatedAt(Instant.now());
//        updateJob.setUpdatedBy(userService.getCurrentUser(connectedUser).getId());
        jobRepository.save(updateJob);
    }

    public void deleteJob(Integer id) {
        Job job = findJobById(id);
        job.setDeleteFlag(true);
        jobRepository.save(job);
    }

    public List<JobResponseModel> getTopJobs(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());

        Page<Job> pagedResult = jobRepository.findAllJob(paging);

        List<JobResponseModel> list = pagedResult.getContent()
                .stream().filter(j -> !j.isDeleteFlag())
                .map(jobMapper::jobToResponseModel)
                .collect(Collectors.toList());

        if (pagedResult.hasContent()) {
            return list;
        } else {
            return new ArrayList<>();
        }
    }

//    public List<Job> findAllJobsAdmin() {
//        return jobRepository.findAll().stream().filter(j -> !j.isDeleteFlag()).collect(Collectors.toList());
//    }

    public Job findJobByIdAdmin(Integer id) {
        return jobRepository.findById(id).filter(j -> !j.isDeleteFlag()).orElseThrow(() -> new ResourceNotFoundException("Job with id " + id + " does not exist"));
    }
}
