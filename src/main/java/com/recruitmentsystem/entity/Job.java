package com.recruitmentsystem.entity;

import com.recruitmentsystem.common.myEnum.JobLevel;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
//@RequiredArgsConstructor
@ToString
@Builder
//@NamedEntityGraphs({
//        @NamedEntityGraph(name = "job_company.all",
//                attributeNodes = {
//                        @NamedAttributeNode(value = "jobId"),
//                        @NamedAttributeNode(value = "jobName"),
//                        @NamedAttributeNode(value = "branch"),
//                        @NamedAttributeNode(value = "jobName"),
//                        @NamedAttributeNode(value = "jobName"),
//                        @NamedAttributeNode(value = "jobName"),
//                        @NamedAttributeNode(value = "jobName"),
//                        @NamedAttributeNode(value = "jobName"),
//                        @NamedAttributeNode(value = "jobName"),
//                        @NamedAttributeNode(value = "jobName"),
//                        @NamedAttributeNode(value = "jobName"),
//                        @NamedAttributeNode(value = "jobName"),
//                        @NamedAttributeNode(value = "jobName"),
//                        @NamedAttributeNode(value = "jobName"),
//
//                })
//})
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
//    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    @JoinColumn(name = "job_level_id")
//    private JobLevel jobLevel;

    @Enumerated(EnumType.STRING)
    private JobLevel jobLevel;

    //    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    @JoinColumn(name = "user_id")
//    private User user;
    private String jobDescription;
    private String jobRequirement;
    private String salary;
    private Instant expiresDate;
    private String jobUrl;

    public Job(Integer jobId,
               String jobName,
               CompanyBranch branch,
               Set<Category> categories,
               JobLevel jobLevel,
               String jobDescription,
               String jobRequirement,
               String salary,
               Instant expiresDate,
               String jobUrl,
               Instant createdAt,
               Instant updatedAt,
               Integer createdBy,
               Integer updatedBy,
               boolean deleteFlag,
               Integer oldId) {
        super(createdAt, updatedAt, createdBy, updatedBy, deleteFlag, oldId);
        this.jobId = jobId;
        this.jobName = jobName;
        this.branch = branch;
        this.categories = categories;
        this.jobLevel = jobLevel;
        this.jobDescription = jobDescription;
        this.jobRequirement = jobRequirement;
        this.salary = salary;
        this.expiresDate = expiresDate;
        this.jobUrl = jobUrl;
    }

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
        this.expiresDate = job.getExpiresDate();
        this.jobUrl = job.getJobUrl();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Job job = (Job) o;
        return getJobId() != null && Objects.equals(getJobId(), job.getJobId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
