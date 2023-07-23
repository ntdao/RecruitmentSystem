package com.recruitmentsystem.entity;

import com.recruitmentsystem.common.myEnum.JobLevelName;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@Builder
public class JobLevel extends Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Integer jobLevelId;
    @Enumerated(EnumType.STRING)
    private JobLevelName jobLevelName;

    public JobLevel(JobLevel updateJobLevel, Integer id, boolean deleteFlag) {
    }
}
