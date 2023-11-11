package com.recruitmentsystem.jobposition;

import org.springframework.stereotype.Service;

@Service
public class JobPositionMapper {
    public JobPositionResponseModel jobPositionToResponseModel(JobPosition jobPosition) {
        return JobPositionResponseModel
                .builder()
                .id(jobPosition.getJobPositionId())
                .nameEN(jobPosition.getJobPositionName())
                .nameVI(jobPosition.getJobPositionNameVI())
                .build();
    }

    public JobPosition jobPositionRequestModelToJobPosition(JobPositionRequestModel request) {
        return JobPosition
                .builder()
                .jobPositionName(request.nameEN())
                .jobPositionNameVI(request.nameVI())
                .build();
    }
}
