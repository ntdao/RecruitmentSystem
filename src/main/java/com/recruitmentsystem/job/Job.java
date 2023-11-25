package com.recruitmentsystem.job;

import com.recruitmentsystem.address.address.Address;
import com.recruitmentsystem.category.Category;
import com.recruitmentsystem.common.myEnum.Gender;
import com.recruitmentsystem.common.myEnum.JobStatus;
import com.recruitmentsystem.company.Company;
import com.recruitmentsystem.jobposition.JobPosition;
import com.recruitmentsystem.jobtype.JobType;
import com.recruitmentsystem.skill.Skill;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
//@Table
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Integer jobId;
    @Column(nullable = false)
    private String jobName;
    @Column(columnDefinition = "text")
    private String jobDescription;
    @Column(columnDefinition = "text")
    private String jobBenefit;
    @Column(columnDefinition = "text")
    private String jobRequirement;
    private boolean isSalaryVisible;
    private String salary;
    private Integer salaryMin;
    private Integer salaryMax;
    private LocalDateTime jobExpiredDate;
    private String jobUrl;
    private String jobTag;
    private Integer jobQuantity;
    @Enumerated(EnumType.STRING)
    private Gender jobGender;
    @Column(columnDefinition = "text")
    private String jobExperience;
    //    @Column(columnDefinition = "text")
//    private String jobAddress;
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JoinTable(
            name = "job_address",
            joinColumns = @JoinColumn(name = "job_id"),
            inverseJoinColumns = @JoinColumn(name = "address_id")
    )
    private Set<Address> jobAddresses;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "company_id")
    private Company company;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "job_position_id")
    private JobPosition jobPosition;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "job_type_id")
    private JobType jobType;
    //    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "job_status_id")
//    private JobStatus jobStatus;
    @Enumerated(EnumType.STRING)
    private JobStatus jobStatus;
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY) // fetch type mặc định là LAZY
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JoinTable(
            name = "job_skill",
            joinColumns = @JoinColumn(name = "job_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private Set<Skill> jobSkills;
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createDate;

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime lastModified;

    @CreatedBy
    @Column(nullable = false, updatable = false)
    private Integer createdBy;

    @LastModifiedBy
    @Column(insertable = false)
    private Integer lastModifiedBy;

    private boolean deleteFlag = false;
    private Integer oldId;

    public Job(Integer id, Job job, boolean deleteFlag) {
        this.jobName = job.getJobName();
        this.jobDescription = job.getJobDescription();
        this.jobBenefit = job.getJobBenefit();
        this.jobRequirement = job.getJobRequirement();
        this.salary = job.getSalary();
        this.salaryMin = job.getSalaryMin();
        this.salaryMax = job.getSalaryMax();
        this.jobExpiredDate = job.getJobExpiredDate();
        this.jobUrl = job.getJobUrl();
        this.jobTag = job.getJobTag();
        this.jobQuantity = job.getJobQuantity();
        this.jobGender = job.getJobGender();
        this.jobExperience = job.getJobExperience();
        this.jobAddresses = job.getJobAddresses();
        this.company = job.getCompany();
        this.category = job.getCategory();
        this.jobPosition = job.getJobPosition();
        this.jobType = job.getJobType();
        this.jobStatus = job.getJobStatus();
        this.jobSkills = job.getJobSkills();
        this.createDate = job.getCreateDate();
        this.lastModified = job.getLastModified();
        this.createdBy = job.getCreatedBy();
        this.lastModifiedBy = job.getLastModifiedBy();
        this.deleteFlag = deleteFlag;
        this.oldId = id;
    }
}
