package com.recruitmentsystem.job;

import com.recruitmentsystem.common.exception.ResourceNotFoundException;
import com.recruitmentsystem.common.enums.JobStatus;
import com.recruitmentsystem.company.Company;
import com.recruitmentsystem.company.CompanyRepository;
import com.recruitmentsystem.company.CompanyService;
import com.recruitmentsystem.pagination.PageDto;
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
    private final CompanyRepository companyRepository;
    private final CompanyService companyService;

    @Transactional
    public void addJob(JobRequestModel request) {
//        companyService.existsById(request.companyId());
//
//        Job job = jobMapper.jobRequestModelToJob(request);
//
//        var companyProxy = companyRepository.getReferenceById(request.companyId());
//        System.out.println(companyProxy);
//        job.setCompany(companyProxy);
//
//        jobRepository.save(job);
    }

    @Transactional
    public void addJob(JobRequestModel request, Principal principal) {
        Company company = companyService.getCurrentCompany(principal);
        Job job = jobMapper.jobRequestModelToJob(request);
        job.setCompany(company);
        jobRepository.save(job);
    }

    public List<JobResponseModel> findAllJob() {
        return jobRepository.findAllJob()
                .stream()
                .filter(j -> j.getJobStatus() == JobStatus.RECRUITING)
                .map(jobMapper::jobToResponseModel)
                .collect(Collectors.toList());
    }

    public List<JobResponseModel> findAllJobsByAdmin() {
        return jobRepository.findAllJob()
                .stream()
                .map(jobMapper::jobToResponseModel)
                .collect(Collectors.toList());
    }

    private List<Job> findByCompanyId(Integer companyId) {
        companyService.existsById(companyId);
        return jobRepository.findAllJobByCompany(companyId);
    }

    public List<JobResponseModel> findJobByCompany(Principal connectedAccount) {
        Company company = companyService.getCurrentCompany(connectedAccount);
        return findByCompanyId(company.getCompanyId())
                .stream()
                .map(jobMapper::jobToResponseModel)
                .toList();
    }

    public List<JobResponseModel> findJobByCompanyId(Integer companyId) {
        return findByCompanyId(companyId)
                .stream()
                .filter(j -> j.getJobStatus() == JobStatus.RECRUITING)
                .map(jobMapper::jobToResponseModel)
                .toList();
    }

    public Job findById(Integer id) {
        return jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job with id " + id + " does not exist"));
    }

    public JobResponseModel findJobById(Integer id) {
        return jobMapper.jobToResponseModel(findById(id));
    }

    public List<JobResponseModel> findJobByJobName(String name) {
        return jobRepository.findByName(name)
                .stream()
                .map(jobMapper::jobToResponseModel)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateJob(Integer id, JobRequestModel requestModel) {
        Job updateJob = findById(id);
        Job oldJob = new Job(id, updateJob, true);
        jobRepository.save(oldJob);

        updateJob = jobMapper.jobRequestModelToJob(requestModel);
        updateJob.setJobId(id);
        jobRepository.save(updateJob);
    }

    public void deleteJob(Integer id) {
        Job job = findById(id);
        job.setDeleteFlag(true);
        jobRepository.save(job);
    }

    public List<JobTopModel> getTopJobs(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());

        Page<JobTopModel> pagedResult = jobRepository.findTopJob(paging);

        if (pagedResult.hasContent()) {
            return pagedResult.getContent();
        } else {
            return new ArrayList<>();
        }
    }

    public List<JobResponseModel> getJobByPaging(PageDto pageDto) {
        Pageable paging = PageRequest.of(
                pageDto.getPageNo(),
                pageDto.getPageSize(),
                Sort.Direction.fromString(pageDto.getSortDir()),
                pageDto.getSortBy());

        Page<Job> pagedResult;
        if (pageDto.getCategories().size() == 0) {
            pagedResult = jobRepository.findJobByName(pageDto.getName(), paging);
        } else {
            pagedResult = jobRepository.findJobByNameAndCategory(pageDto.getName(), pageDto.getCategories(), paging);
        }
        if (pagedResult.hasContent()) {
            return pagedResult.getContent()
                    .stream()
                    .map(jobMapper::jobToResponseModel)
                    .toList();
        } else {
            return new ArrayList<>();
        }
    }
}
