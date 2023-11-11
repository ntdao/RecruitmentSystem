package com.recruitmentsystem.job;

import com.recruitmentsystem.category.CategoryService;
import com.recruitmentsystem.jobposition.JobPositionService;
import com.recruitmentsystem.jobtype.JobTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JobMapper {
    private final CategoryService categoryService;
    private final JobPositionService jobPositionService;
    private final JobTypeService jobTypeService;
    private final SalaryService salaryService;
    public JobResponseModel jobToResponseModel(Job job) {
        return JobResponseModel
                .builder()
                .id(job.getJobId())
                .name(job.getJobName())
                .companyName(job.getCompany().getCompanyFullName())
                .companyLogo(job.getCompany().getCompanyLogo())
//                .jobAddress(job.getBranch().getBranchAddress())
                .jobPosition(job.getJobPosition().toString())
                .salary(job.getSalary())
                .jobUrl(job.getJobUrl())
                .jobRequirement(job.getJobRequirement())
                .jobDescription(job.getJobDescription())
//                .jobBenefit(job.getJobBenefit())
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
                .jobAddress(request.jobAddress())
                .jobPosition(jobPositionService.findByName(request.jobPosition()))
                .salary(salaryService.salaryRequestToResponse(request.isSalaryVisible(), request.salaryMax(), request.salaryMin()))
                .jobUrl(request.jobUrl())
                .jobRequirement(request.jobRequirement())
                .jobDescription(request.jobDescription())
                .jobType(jobTypeService.findByName(request.jobType()))
                .category(categoryService.findByName(request.category()))
                .jobExpiredDate(request.jobDeadline())
                .build();
    }
}
