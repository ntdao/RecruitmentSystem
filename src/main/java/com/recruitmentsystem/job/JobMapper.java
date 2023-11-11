package com.recruitmentsystem.job;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JobMapper {
    public JobResponseModel jobToResponseModel(Job job) {
        return JobResponseModel
                .builder()
                .id(job.getJobId())
                .name(job.getJobName())
                .companyName(job.getCompany().getCompanyFullName())
                .companyLogo(job.getCompany().getCompanyLogo())
//                .jobAddress(job.getBranch().getBranchAddress())
                .jobLevel(job.getJobPosition().toString())
                .salary(job.getSalary())
                .jobUrl(job.getJobUrl())
                .jobRequirement(job.getJobRequirement())
                .jobDescription(job.getJobDescription())
                .jobBenefit(job.getJobBenefit())
                .jobTag(job.getJobTag())
                .jobType(job.getJobType().toString())
                .jobGender(job.getJobGender().toString())
                .jobQuantity(job.getJobQuantity())
                .jobExperience(job.getJobExperience().toString())
                .jobStatus(job.getJobStatus().toString())
                .category(job.getCategory().getCategoryName())
                .jobExpiredDate(job.getJobExpiredDate())
                .createdAt(job.getCreateDate())
                .build();
    }

    public Job jobRequestModelToJob(JobRequestModel request) {
        return Job
                .builder()
                .jobName(request.name())
//                .branch(request.companyName())
//                .jobAddress(job.getBranch().getBranchAddress())
//                .jobPosition(request.jobPosition())
                .salary(request.salaryMin() + " - " + request.salaryMax())
                .jobUrl(request.jobUrl())
                .jobRequirement(request.jobRequirement())
                .jobDescription(request.jobDescription())
                .jobBenefit(request.jobBenefit())
//                .jobType(request.jobType())
//                .category(request.get)
                .jobExpiredDate(request.jobDeadline())
                .build();
    }
}
