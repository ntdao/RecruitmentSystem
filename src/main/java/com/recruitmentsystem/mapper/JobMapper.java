package com.recruitmentsystem.mapper;

import com.recruitmentsystem.entity.Job;
import com.recruitmentsystem.model.job.JobDisplayModel;
import com.recruitmentsystem.model.job.JobRequestModel;
import org.springframework.stereotype.Service;

@Service
public class JobMapper {
    public JobDisplayModel jobToDisplayModel(Job job) {
        return JobDisplayModel
                .builder()
                .name(job.getJobName())
                .companyLogo(job.getBranch().getCompany().getCompanyLogo())
                .jobAddress(job.getBranch().getBranchAddress())
                .createdAt(job.getCreatedAt())
                .build();
    }

    public Job jobRequestModelToJob(JobRequestModel request) {
        return Job
                .builder()
                .jobName(request.name())
                .build();
    }
}
