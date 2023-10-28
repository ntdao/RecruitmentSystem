package com.recruitmentsystem.mapper;

import com.recruitmentsystem.common.myEnum.JobType;
import com.recruitmentsystem.entity.Category;
import com.recruitmentsystem.entity.Job;
import com.recruitmentsystem.model.job.JobDisplayModel;
import com.recruitmentsystem.model.job.JobRequestModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class JobMapper {
    public JobDisplayModel jobToDisplayModel(Job job) {
        Set<Category> categories = job.getCategories();
        List<String> catList = new ArrayList<>();
        for (Category c : categories) {
            catList.add(c.getCategoryName());
        }
        return JobDisplayModel
                .builder()
                .id(job.getJobId())
                .name(job.getJobName())
                .branch(job.getBranch().getBranchName())
                .companyName(job.getBranch().getCompany().getCompanyFullName())
                .companyLogo(job.getBranch().getCompany().getCompanyLogo())
                .jobAddress(job.getBranch().getBranchAddress())
                .jobLevel(job.getJobLevel().toString())
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
                .category(catList)
                .jobDeadline(job.getJobDeadline())
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
                .salary(request.salaryMin() + " - "+ request.salaryMax())
                .jobUrl(request.jobUrl())
                .jobRequirement(request.jobRequirement())
                .jobDescription(request.jobDescription())
                .jobBenefit(request.jobBenefit())
                .jobType(JobType.fromString(request.jobType()))
//                .category(request.getCategories().toString())
                .jobDeadline(request.jobDeadline())
                .build();
    }
}
