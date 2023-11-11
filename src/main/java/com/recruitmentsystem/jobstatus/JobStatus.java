package com.recruitmentsystem.jobstatus;

import jakarta.persistence.*;
import lombok.*;

@Entity
//@Table
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class JobStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Integer jobStatusId;
    @Column(nullable = false)
    private String jobStatusName;
    @Column(name = "job_status_name_vi")
    private String jobStatusNameVI;
}
