package hust.seee.recruitmentsystem.entity;

import hust.seee.recruitmentsystem.enums.Gender;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
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
    private String salary;
    private LocalDateTime jobExpiredDate;
    private Integer jobQuantity;
    @Enumerated(EnumType.STRING)
    private Gender jobGender;
    @Column(columnDefinition = "text")
    private String jobExperience;
    private String minEducationLevel;
    //    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @EqualsAndHashCode.Exclude
//    @ToString.Exclude
//    @JoinTable(
//            name = "job_address",
//            joinColumns = @JoinColumn(name = "job_id"),
//            inverseJoinColumns = @JoinColumn(name = "address_id")
//    )
//    private Set<Address> jobAddresses;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "address_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Address jobAddress;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "company_id")
    private Company company;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "job_type_id")
    private JobType jobType;
    // 0 - Đang tuyển, 1 - Hoàn thành, 2 - Hết hạn
    private Integer jobStatus;
    @ManyToMany(cascade = CascadeType.ALL) // fetch type mặc định là LAZY
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
        this.jobExpiredDate = job.getJobExpiredDate();
        this.jobQuantity = job.getJobQuantity();
        this.jobGender = job.getJobGender();
        this.jobExperience = job.getJobExperience();
        this.jobAddress = job.getJobAddress();
        this.company = job.getCompany();
        this.category = job.getCategory();
        this.jobType = job.getJobType();
        this.jobStatus = job.getJobStatus();
        this.minEducationLevel = job.getMinEducationLevel();
        this.jobSkills = job.getJobSkills();
        this.createDate = job.getCreateDate();
        this.lastModified = job.getLastModified();
        this.createdBy = job.getCreatedBy();
        this.lastModifiedBy = job.getLastModifiedBy();
        this.deleteFlag = deleteFlag;
        this.oldId = id;
    }
}
