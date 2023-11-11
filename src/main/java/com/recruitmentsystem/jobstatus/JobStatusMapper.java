package com.recruitmentsystem.jobstatus;

import org.springframework.stereotype.Service;

@Service
public class JobStatusMapper {
    public JobStatusResponseModel jobStatusToResponseModel(JobStatus jobStatus) {
        return JobStatusResponseModel
                .builder()
                .id(jobStatus.getJobStatusId())
                .nameEN(jobStatus.getJobStatusName())
                .nameVI(jobStatus.getJobStatusNameVI())
                .build();
    }

    public JobStatus jobStatusRequestModelToJobStatus(JobStatusRequestModel request) {
        return JobStatus
                .builder()
                .jobStatusName(request.nameEN())
                .jobStatusNameVI(request.nameVI())
                .build();
    }
}
