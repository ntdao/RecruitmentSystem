package com.recruitmentsystem.job;

import com.recruitmentsystem.common.exception.ResourceNotFoundException;
import com.recruitmentsystem.company.Company;
import com.recruitmentsystem.company.CompanyService;
import com.recruitmentsystem.job.Job;
import com.recruitmentsystem.job.JobMapper;
import com.recruitmentsystem.job.JobResponseModel;
import com.recruitmentsystem.job.JobRequestModel;
import com.recruitmentsystem.job.JobRepository;
import com.recruitmentsystem.user.UserService;
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
    private final UserService userService;
    private final CompanyService companyService;

    public void addJob(JobRequestModel requestModel, Principal connectedUser) {
        Job job = jobMapper.jobRequestModelToJob(requestModel);
        jobRepository.save(job);
    }

    public List<JobResponseModel> findAllJobs() {
        List<Job> jobs = jobRepository.findAll();
        return jobs.stream().filter(job -> !job.isDeleteFlag()
                        && job.getJobStatus().toString().equals("RECRUITING"))
                .map(jobMapper::jobToResponseModel).collect(Collectors.toList());
    }

    public List<JobResponseModel> findAllJobsByAdmin() {
        List<Job> jobs = jobRepository.findAll();
        return jobs.stream().filter(job -> !job.isDeleteFlag())
                .map(jobMapper::jobToResponseModel).collect(Collectors.toList());
    }

    public List<JobResponseModel> findAllJobsByCompany(int companyId) {
        Company company = companyService.findCompanyById(companyId);
        System.out.println("Company: " + company);
        List<Integer> list = jobRepository.findAllJobByCompany(companyId);
        System.out.println(list);
        List<JobResponseModel> jobs = findAllJobsByIds(list)
                .stream()
                .filter(job -> !job.isDeleteFlag())
                .map(jobMapper::jobToResponseModel).collect(Collectors.toList());
        return jobs;
    }

//    public List<JobResponseModel> findAllJobsByBranch(int branchId) {
//        CompanyBranch branch = branchService.findBranchById(branchId);
//        System.out.println("Branch: " + branch);
//        List<Integer> list = jobRepository.findAllJobByBranch(branchId);
//        System.out.println(list);
//        List<JobResponseModel> jobs = findAllJobsByIds(list)
//                .stream()
//                .filter(job -> !job.isDeleteFlag())
//                .map(jobMapper::jobToResponseModel).collect(Collectors.toList());
//        return jobs;
//    }

//    public List<JobResponseModel> findAllJobsByHR(Principal connectedUser) {
//        int hrId = userService.getCurrentUser(connectedUser).getUserId();
//        List<Integer> list = jobRepository.findAllJobByHR(hrId);
//        System.out.println(list);
//        List<JobResponseModel> jobs = findAllJobsByIds(list)
//                .stream()
//                .filter(job -> !job.isDeleteFlag())
//                .map(jobMapper::jobToResponseModel).collect(Collectors.toList());
//        return jobs;
//    }

    public List<Job> findAllJobsByIds(List<Integer> list) {
        if (list.get(0) == null) {
            throw new ResourceNotFoundException("No job can found");
        }
        List<Job> jobs = new ArrayList<>();
        for (Integer i : list) {
            jobs.add(findJobById(i));
        }
        System.out.println(jobs);
        return jobs;
    }

    public List<JobResponseModel> findAllByName(String name) {
        List<Job> jobs = jobRepository.findAll();
        return jobs.stream().filter(job -> (!job.isDeleteFlag() && job.getJobName().contains(name))).map(jobMapper::jobToResponseModel).collect(Collectors.toList());
    }

    public JobResponseModel findById(Integer id) {
        return jobRepository.findById(id)
                .filter(job -> !job.isDeleteFlag())
                .map(jobMapper::jobToResponseModel)
                .orElseThrow(() -> new ResourceNotFoundException("Job with id " + id + " does not exist"));
    }

    public Job findJobById(Integer id) {
        return jobRepository.findById(id).filter(job -> !job.isDeleteFlag()).orElseThrow(() -> new ResourceNotFoundException("Job with id " + id + " does not exist"));
    }

    public List<Job> findJobByJobNameAdmin(String name) {
        return jobRepository.findByJobNameContaining(name).stream().filter(j -> !j.isDeleteFlag()).collect(Collectors.toList());
    }

    public List<JobResponseModel> findJobByJobName(String name) {
        return jobRepository.findByJobNameContaining(name).stream().filter(j -> !j.isDeleteFlag()).map(jobMapper::jobToResponseModel).collect(Collectors.toList());
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

    public void deleteJob(Integer id, Principal connectedUser) {
        Job job = findJobById(id);
        job.setDeleteFlag(true);
//        job.setUpdatedAt(Instant.now());
//        job.setUpdatedBy(userService.getCurrentUser(connectedUser).getId());
        jobRepository.save(job);
    }

    public List<JobResponseModel> getTopJobs(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());

        Page<Job> pagedResult = jobRepository.findAll(paging);

        List<JobResponseModel> list = pagedResult.getContent().stream().filter(j -> !j.isDeleteFlag()).map(jobMapper::jobToResponseModel).collect(Collectors.toList());

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
