package com.recruitmentsystem.jobposition;

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
public class JobPosition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Integer jobPositionId;
    @Column(nullable = false)
    private String jobPositionName;
    @Column(name = "job_position_name_vi")
    private String jobPositionNameVI;
    @OneToMany(mappedBy = "jobPosition", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Job> jobs;
}

