package com.recruitmentsystem.jobtype;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class JobType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Integer jobTypeId;
    @Column(nullable = false)
    private String jobTypeName;
    @Column(name = "job_type_name_vi")
    private String jobTypeNameVI;
}
