//package com.recruitmentsystem.mapper;
//
//import com.recruitmentsystem.entity.JobLevel;
//import com.recruitmentsystem.model.jobLevel.JobLevelDisplayModel;
//import com.recruitmentsystem.model.jobLevel.JobLevelRequestModel;
//import org.springframework.stereotype.Service;
//
//@Service
//public class JobLevelMapper {
//    public JobLevelDisplayModel jobLevelToDisplayModel(JobLevel jobLevel) {
//        return JobLevelDisplayModel
//                .builder()
//                .id(jobLevel.getJobLevelId())
//                .name(jobLevel.getJobLevelName())
//                .createdAt(jobLevel.getCreatedAt())
//                .updatedAt(jobLevel.getUpdatedAt())
//                .build();
//    }
//
//    public JobLevel jobLevelRequestModelToJobLevel(JobLevelRequestModel request) {
//        return JobLevel
//                .builder()
//                .jobLevelName(request.name())
//                .build();
//    }
//}
