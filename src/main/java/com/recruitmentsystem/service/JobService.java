package com.recruitmentsystem.service;

import com.recruitmentsystem.common.exception.ResourceNotFoundException;
import com.recruitmentsystem.entity.Job;
import com.recruitmentsystem.mapper.JobMapper;
import com.recruitmentsystem.model.job.JobDisplayModel;
import com.recruitmentsystem.model.job.JobRequestModel;
import com.recruitmentsystem.repository.IJobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobService {
    private final IJobRepository jobRepository;
    private final JobMapper jobMapper;

    public void addJob(JobRequestModel request) {
        // add
        Job job = jobMapper.jobRequestModelToJob(request);
        job.setCreatedAt(Instant.now());
        jobRepository.save(job);
    }

    public List<JobDisplayModel> findAllJobes() {
        List<Job> jobs = jobRepository.findAll();
        return jobs.stream()
                .filter(job -> !job.isDeleteFlag())
                .map(jobMapper::jobToDisplayModel)
                .collect(Collectors.toList());
    }

    public List<JobDisplayModel> findAllByName(String name) {
        List<Job> jobs = jobRepository.findAll();
        return jobs.stream()
                .filter(job ->
                        (!job.isDeleteFlag() &&
                        job.getJobName().equals(name)))
                .map(jobMapper::jobToDisplayModel)
                .collect(Collectors.toList());
    }

    public JobDisplayModel findById(Integer id) {
        return jobRepository.findById(id)
                .filter(job -> !job.isDeleteFlag())
                .map(jobMapper::jobToDisplayModel)
                .orElseThrow(() -> new ResourceNotFoundException("Job with id " + id + " does not exist"));
    }

    public Job findJobById(Integer id) {
        return jobRepository.findById(id)
                .filter(job -> !job.isDeleteFlag())
                .orElseThrow(() -> new ResourceNotFoundException("Job with id " + id + " does not exist"));
    }

    public List<JobDisplayModel> findJobByJobName(String name) {
//        return jobRepository.findByJobName(name)
//                .filter(job -> !job.isDeleteFlag())
//                .orElseThrow(() -> new ResourceNotFoundException("Job with name " + name + " does not exist"));
        return jobRepository.findAll()
                .stream()
                .filter(b -> (!b.isDeleteFlag() && b.getJobName().contains(name)))
                .map(jobMapper::jobToDisplayModel)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateJob(Integer id, JobRequestModel requestModel) {
        // tim job theo id
        Job updateJob = findJobById(id);
//        updateJob.setUpdatedAt(LocalDateTime.now());

        // tao ban ghi luu thong tin cu cua job
        Job oldJob = Job.builder()
                .jobName(updateJob.getJobName())
                .jobLevel(updateJob.getJobLevel())
                .jobUrl(updateJob.getJobUrl())
                .jobRequirement(updateJob.getJobRequirement())
                .jobDescription(updateJob.getJobDescription())
                .branch(updateJob.getBranch())
                .user(updateJob.getUser())
                .categories(updateJob.getCategories())
                .branch(updateJob.getBranch())
                .salary(updateJob.getSalary())
                .expiresDate(updateJob.getExpiresDate())
                .build();
        jobRepository.save(oldJob);

        // update job
        updateJob = jobMapper.jobRequestModelToJob(requestModel);
        updateJob.setJobId(id);
        updateJob.setCreatedAt(oldJob.getCreatedAt());
        updateJob.setCreatedBy(oldJob.getCreatedBy());
        updateJob.setUpdatedAt(oldJob.getUpdatedAt());
//        updateJob.setUpdatedBy();
        jobRepository.save(updateJob);
    }

    public void deleteJob(Integer id) {
        Job job = findJobById(id);
        job.setDeleteFlag(true);
        jobRepository.save(job);
    }
}
