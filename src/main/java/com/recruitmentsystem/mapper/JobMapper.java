package com.recruitmentsystem.mapper;

import com.recruitmentsystem.entity.Job;
import com.recruitmentsystem.model.job.JobDisplayModel;
import com.recruitmentsystem.model.job.JobRequestModel;
import com.recruitmentsystem.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JobMapper {
//    private final CompanyService companyService;
    public JobDisplayModel jobToDisplayModel(Job job) {
        return JobDisplayModel
                .builder()
                .name(job.getJobName())
                .companyLogo(job.getBranch().getCompany().getCompanyLogo())
                .jobAddress(job.getBranch().getBranchAddress())
                .jobLevel(job.getJobLevel())
                .salary(job.getSalary())
                .jobUrl(job.getJobUrl())
                .jobRequirement(job.getJobRequirement())
                .jobDescription(job.getJobDescription())
                .category(job.getCategories().toString())
                .expiresDate(job.getExpiresDate())
                .createdAt(job.getCreatedAt())
                .build();
    }

    public Job jobRequestModelToJob(JobRequestModel request) {
        return Job
                .builder()
                .jobName(request.name())
//                .branch(request.companyName())
//                .jobAddress(job.getBranch().getBranchAddress())
                .jobLevel(request.jobLevel())
                .salary(request.salary())
                .jobUrl(request.jobUrl())
                .jobRequirement(request.jobRequirement())
                .jobDescription(request.jobDescription())
//                .category(request.getCategories().toString())
                .expiresDate(request.expiresDate())
                .build();
    }
}
