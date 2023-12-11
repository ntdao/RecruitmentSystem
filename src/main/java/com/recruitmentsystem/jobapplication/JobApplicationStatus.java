//package com.recruitmentsystem.jobapplication;
//
//import com.recruitmentsystem.application.Application;
//import com.recruitmentsystem.job.Job;
//import jakarta.persistence.*;
//import lombok.*;
//
//@Entity
//@NoArgsConstructor
//@AllArgsConstructor
//@Getter
//@Setter
//@ToString
//@Builder
//public class JobApplicationStatus {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(updatable = false)
//    private Integer jobApplicationStatusId;
//    @Column(nullable = false)
//    private String status;
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "job_id")
//    private Job job;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "application_id")
//    private Application application;
//}
