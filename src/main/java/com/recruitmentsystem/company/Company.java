package com.recruitmentsystem.company;

import com.recruitmentsystem.account.Account;
import com.recruitmentsystem.address.address.Address;
import com.recruitmentsystem.industry.Industry;
import com.recruitmentsystem.job.Job;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
//@Table
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Integer companyId;
    @Column(nullable = false)
    private String companyShortName;
    @Column(nullable = false)
    private String companyFullName;
    private String companyLogo;
    private String companyUrl;
    @Column(columnDefinition = "text")
    private String companyIntroduction;
    private String companyImage;
//    private String companyAddress;
    @OneToOne()
    @JoinColumn(name = "address_id")
    private Address address;
    // fetch = LAZY -> error lazy init...
    @ManyToOne() // fetch type mặc định là EAGER
    @JoinColumn(name = "industry_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Industry industry;
    private Integer companyFoundedYear;
    private String companyMst;
    private String companyLicense;
    private String companySize;
    private String companyTag;
    private boolean isActive;
//    @PrimaryKeyJoinColumn
    @OneToOne(cascade = CascadeType.MERGE)
    private Account account;
//    private String email;
    private String phoneNumber;
    private String website;
    private String facebookUrl;
    private String youtubeUrl;
    private String linkedinUrl;
    private String companyBranch;
//    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
//    @EqualsAndHashCode.Exclude
//    @ToString.Exclude
//    private List<Job> jobs;
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

//    @OneToMany(mappedBy = "company")
//    private Set<CompanyBranch> branches;

//    @OneToMany(mappedBy = "company")
//    Set<CompanyBenefit> benefitDetail;

    public Company(Company company, boolean deleteFlag) {
//        super(company.getEmail(),
//                company.getPassword(),
//                company.getRole());
        this.companyShortName = company.getCompanyShortName();
        this.companyFullName = company.getCompanyFullName();
        this.companyLogo = company.getCompanyLogo();
        this.companyIntroduction = company.getCompanyIntroduction();
        this.companyImage = company.getCompanyImage();
        this.address = company.getAddress();
        this.industry = company.getIndustry();
        this.companyFoundedYear = company.getCompanyFoundedYear();
        this.companyMst = company.getCompanyMst();
        this.companyLicense = company.getCompanyLicense();
        this.companySize = company.getCompanySize();
        this.companyTag = company.getCompanyTag();
        this.isActive = company.isActive();
//        this.email = company.getEmail();
        this.phoneNumber = company.getPhoneNumber();
        this.website = company.getWebsite();
        this.facebookUrl = company.getFacebookUrl();
        this.youtubeUrl = company.getYoutubeUrl();
        this.linkedinUrl = company.getLinkedinUrl();
        this.companyUrl = company.getCompanyUrl();
//        this.benefitDetail = company.getBenefitDetail();
        this.createDate = company.getCreateDate();
        this.createdBy = company.getCreatedBy();
        this.lastModified = company.getLastModified();
        this.lastModifiedBy = company.getLastModifiedBy();
        this.deleteFlag = deleteFlag;
        this.oldId = company.getCompanyId();
    }
}
