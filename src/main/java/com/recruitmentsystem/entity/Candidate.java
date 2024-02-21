package com.recruitmentsystem.entity;

import com.recruitmentsystem.enums.Gender;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Candidate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "candidate_id")
    private Integer candidateId;
    @OneToOne(cascade = CascadeType.MERGE)
    private Account account;
    private String fullName;
    private String phoneNumber;
    @OneToOne
    @JoinColumn(name = "address_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Address address;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private String imgUrl;
    private LocalDate birthday;
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JoinTable(
            name = "candidate_skill",
            joinColumns = @JoinColumn(name = "candidate_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private Set<Skill> candidateSkills;
    @OneToMany(mappedBy = "candidate")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<CandidateEducation> candidateEducations = new HashSet<>();
    @OneToMany(mappedBy = "candidate")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<CandidateWorkingHistory> workingHistories = new HashSet<>();
    @OneToOne
    @JoinColumn(name = "category_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Category category;
    private String desiredJob;
    private String cvUrl;
    private String educationLevel;
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

    public Candidate(Candidate candidate, boolean deleteFlag) {
        this.fullName = candidate.getFullName();
        this.phoneNumber = candidate.getPhoneNumber();
        this.address = candidate.getAddress();
        this.gender = candidate.getGender();
        this.imgUrl = candidate.getImgUrl();
        this.birthday = candidate.getBirthday();
        this.account = candidate.getAccount();
        this.candidateSkills = candidate.getCandidateSkills();
        this.candidateEducations = candidate.getCandidateEducations();
        this.workingHistories = candidate.getWorkingHistories();
        this.category = candidate.getCategory();
        this.desiredJob = candidate.getDesiredJob();
        this.cvUrl = candidate.getCvUrl();
        this.educationLevel = candidate.getEducationLevel();
        this.createDate = candidate.getCreateDate();
        this.lastModified = candidate.getLastModified();
        this.createdBy = candidate.getCreatedBy();
        this.lastModifiedBy = candidate.getLastModifiedBy();
        this.deleteFlag = deleteFlag;
        this.oldId = candidate.getCandidateId();
    }
}
