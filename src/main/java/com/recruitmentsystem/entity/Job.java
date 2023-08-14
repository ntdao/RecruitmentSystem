package com.recruitmentsystem.entity;

import com.recruitmentsystem.common.myEnum.JobLevel;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@Builder
public class Job extends Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Integer jobId;

    @Column(nullable = false)
    private String jobName;
//    @Column(nullable = false)
//    private String specFile;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "branch_id")
    private CompanyBranch branch;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "job_category",
            joinColumns = @JoinColumn(name = "job_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories = new HashSet<>();
//    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    @JoinColumn(name = "job_level_id")
//    private JobLevel jobLevel;

    @Enumerated(EnumType.STRING)
    private JobLevel jobLevel;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;
    private String jobDescription;
    private String jobRequirement;
    private String salary;
    private Instant expiresDate;
    private String jobUrl;
}
