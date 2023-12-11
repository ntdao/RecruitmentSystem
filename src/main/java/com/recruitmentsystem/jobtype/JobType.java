package com.recruitmentsystem.jobtype;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.recruitmentsystem.job.Job;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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
    @OneToMany(mappedBy = "jobType", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    private List<Job> jobs;
}
