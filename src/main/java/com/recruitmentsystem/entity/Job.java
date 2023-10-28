package com.recruitmentsystem.entity;

import com.recruitmentsystem.common.myEnum.*;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class Job extends Audit implements Serializable {
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

    @Enumerated(EnumType.STRING)
    private JobLevel jobLevel;

    //    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    @JoinColumn(name = "user_id")
//    private User user;
    private String jobDescription;
    private String jobBenefit;
    private String jobRequirement;
    private String salary;
    private Integer salaryMin;
    private Integer salaryMax;
    private Instant jobDeadline;
    private String jobUrl;
    private String jobTag;
//    private String jobSkill;
    private Integer jobQuantity;
    @Enumerated(EnumType.STRING)
    private Gender jobGender;
    @Enumerated(EnumType.STRING)
    private JobType jobType;
    @Enumerated(EnumType.STRING)
    private JobExperience jobExperience;
    @Enumerated(EnumType.STRING)
    private JobStatus jobStatus;

    public Job(Job job, boolean deleteFlag) {
        super(job.getCreatedAt(),
                job.getUpdatedAt(),
                job.getCreatedBy(),
                job.getUpdatedBy(),
                deleteFlag,
                job.getJobId());
        this.jobName = job.getJobName();
        this.branch = job.getBranch();
        this.categories = job.getCategories();
        this.jobLevel = job.getJobLevel();
        this.jobDescription = job.getJobDescription();
        this.jobRequirement = job.getJobRequirement();
        this.salary = job.getSalary();
        this.jobDeadline = job.getJobDeadline();
        this.jobUrl = job.getJobUrl();
        this.jobQuantity = job.getJobQuantity();
        this.jobBenefit = job.getJobBenefit();
        this.jobGender = job.getJobGender();
        this.jobTag = job.getJobTag();
        this.jobType = job.getJobType();
        this.jobStatus = job.getJobStatus();
        this.jobExperience = job.getJobExperience();
    }
}
