package com.recruitmentsystem.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;
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
            name = "user_skill",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private List<Skill> userSkills;
    @OneToMany(mappedBy = "user")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<UserEducation> userEducations = new HashSet<>();
    @OneToMany(mappedBy = "user")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<UserWorkingHistory> workingHistories = new HashSet<>();
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

    public User(User user, boolean deleteFlag) {
        this.fullName = user.getFullName();
        this.phoneNumber = user.getPhoneNumber();
        this.address = user.getAddress();
        this.gender = user.getGender();
        this.imgUrl = user.getImgUrl();
        this.birthday = user.getBirthday();
        this.account = user.getAccount();
        this.userSkills = user.getUserSkills();
        this.userEducations = user.getUserEducations();
        this.workingHistories = user.getWorkingHistories();
        this.category = user.getCategory();
        this.desiredJob = user.getDesiredJob();
        this.cvUrl = user.getCvUrl();
        this.educationLevel = user.getEducationLevel();
        this.createDate = user.getCreateDate();
        this.lastModified = user.getLastModified();
        this.createdBy = user.getCreatedBy();
        this.lastModifiedBy = user.getLastModifiedBy();
        this.deleteFlag = deleteFlag;
        this.oldId = user.getUserId();
    }
}
