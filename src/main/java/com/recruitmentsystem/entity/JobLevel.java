//package com.recruitmentsystem.entity;
//
//import jakarta.persistence.*;
//import lombok.*;
//
//@Entity
//@Table
//@NoArgsConstructor
//@AllArgsConstructor
//@Data
//@Builder
//public class JobLevel extends Audit {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(updatable = false)
//    private Integer jobLevelId;
//    @Column(nullable = false)
//    private String jobLevelName;
//
//    public JobLevel(JobLevel jobLevel, Integer oldId, boolean deleteFlag) {
//        super(jobLevel.getCreatedAt(),
//                jobLevel.getUpdatedAt(),
//                jobLevel.getCreatedBy(),
//                jobLevel.getUpdatedBy(),
//                deleteFlag,
//                oldId);
//        this.jobLevelName = jobLevel.getJobLevelName();
//    }
//    @Override
//    public String toString() {
//        return this.jobLevelName;
//    }
//}
