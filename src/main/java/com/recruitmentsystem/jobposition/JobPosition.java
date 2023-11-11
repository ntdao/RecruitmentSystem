package com.recruitmentsystem.jobposition;

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
public class JobPosition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Integer jobPositionId;
    @Column(nullable = false)
    private String jobPositionName;
    @Column(name = "job_position_name_vi")
    private String jobPositionNameVI;
}

