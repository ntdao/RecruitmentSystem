//package com.recruitmentsystem.jobtype;
//
//import org.springframework.stereotype.Service;
//
//@Service
//public class JobTypeMapper {
//    public JobTypeResponseModel jobTypeToResponseModel(JobType jobType) {
//        return JobTypeResponseModel
//                .builder()
//                .id(jobType.getJobTypeId())
//                .nameEN(jobType.getJobTypeName())
//                .nameVI(jobType.getJobTypeNameVI())
//                .build();
//    }
//
//    public JobType jobTypeRequestModelToJobType(JobTypeRequestModel request) {
//        return JobType
//                .builder()
//                .jobTypeName(request.nameEN())
//                .jobTypeNameVI(request.nameVI())
//                .build();
//    }
//}
