package com.recruitmentsystem.service;

import com.recruitmentsystem.common.exception.ResourceNotFoundException;
import com.recruitmentsystem.entity.Job;
import com.recruitmentsystem.mapper.JobMapper;
import com.recruitmentsystem.model.job.JobDisplayModel;
import com.recruitmentsystem.model.job.JobRequestModel;
import com.recruitmentsystem.repository.IJobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobService {
    private final IJobRepository jobRepository;
    private final JobMapper jobMapper;
    private final UserService userService;

    public void addJob(JobRequestModel request) {
        Job job = jobMapper.jobRequestModelToJob(request);
        job.setCreatedAt(Instant.now());
        jobRepository.save(job);
    }

    public List<JobDisplayModel> findAllJobs() {
        List<Job> jobs = jobRepository.findAll();
        return jobs.stream().filter(job -> !job.isDeleteFlag()).map(jobMapper::jobToDisplayModel).collect(Collectors.toList());
    }

    public List<Job> findAllJobsByCompany(int companyId) {
        List<Integer> list = jobRepository.findAllJobByCompany(companyId);
        System.out.println(list);
        List<Job> jobs = findAllJobsByIds(list);
        return jobs;
    }

    public List<Job> findAllJobsByBranch(int companyId) {
        List<Integer> list = jobRepository.findAllJobByBranch(companyId);
        System.out.println(list);
        List<Job> jobs = findAllJobsByIds(list);
        return jobs;
    }

    public List<Job> findAllJobsByHR(String token) {
        int hrId = userService.findUserByToken(token).getId();
        List<Integer> list = jobRepository.findAllJobByHR(hrId);
        System.out.println(list);
        List<Job> jobs = findAllJobsByIds(list);
        return jobs;
    }

    public List<Job> findAllJobsByIds(List<Integer> list) {
        List<Job> jobs = new ArrayList<>();
        for (Integer i : list) {
            jobs.add(findJobById(i));
        }
        System.out.println(jobs);
        if (jobs == null) {
            throw new ResourceNotFoundException("No job can found");
        }
        return jobs;
    }

    public List<JobDisplayModel> findAllByName(String name) {
        List<Job> jobs = jobRepository.findAll();
        return jobs.stream().filter(job -> (!job.isDeleteFlag() && job.getJobName().contains(name))).map(jobMapper::jobToDisplayModel).collect(Collectors.toList());
    }

//    public JobDisplayModel findById(Integer id) {
//        return jobRepository.findById(id)
//                .filter(job -> !job.isDeleteFlag())
//                .map(jobMapper::jobToDisplayModel)
//                .orElseThrow(() -> new ResourceNotFoundException("Job with id " + id + " does not exist"));
//    }

    public Job findJobById(Integer id) {
        return jobRepository.findById(id).filter(job -> !job.isDeleteFlag()).orElseThrow(() -> new ResourceNotFoundException("Job with id " + id + " does not exist"));
    }

    public List<Job> findJobByJobNameAdmin(String name) {
        return jobRepository.findByJobNameContaining(name).stream().filter(j -> !j.isDeleteFlag()).collect(Collectors.toList());
    }

    public List<JobDisplayModel> findJobByJobName(String name) {
        return jobRepository.findByJobNameContaining(name).stream().filter(j -> !j.isDeleteFlag()).map(jobMapper::jobToDisplayModel).collect(Collectors.toList());
    }

    @Transactional
    public void updateJob(Integer id, JobRequestModel requestModel) {

        Job updateJob = findJobById(id);
        Job oldJob = new Job(updateJob, true);
        jobRepository.save(oldJob);

        updateJob = jobMapper.jobRequestModelToJob(requestModel);
        updateJob.setJobId(id);
        updateJob.setCreatedAt(oldJob.getCreatedAt());
        updateJob.setCreatedBy(oldJob.getCreatedBy());
        updateJob.setUpdatedAt(Instant.now());
//        updateJob.setUpdatedBy();
        jobRepository.save(updateJob);
    }

    public void deleteJob(Integer id) {
        Job job = findJobById(id);
        job.setDeleteFlag(true);
        jobRepository.save(job);
    }

    public List<JobDisplayModel> getTopJobs(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());

        Page<Job> pagedResult = jobRepository.findAll(paging);

        List<JobDisplayModel> list = pagedResult.getContent().stream().filter(j -> !j.isDeleteFlag()).map(jobMapper::jobToDisplayModel).collect(Collectors.toList());

        if (pagedResult.hasContent()) {
            return list;
        } else {
            return new ArrayList<>();
        }
    }

    public List<Job> findAllJobsAdmin() {
        return jobRepository.findAll().stream().filter(j -> !j.isDeleteFlag()).collect(Collectors.toList());
    }

    public Job findJobByIdAdmin(Integer id) {
        return jobRepository.findById(id).filter(j -> !j.isDeleteFlag()).orElseThrow(() -> new ResourceNotFoundException("Job with id " + id + " does not exist"));
    }
}
