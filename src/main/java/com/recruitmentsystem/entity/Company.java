package com.recruitmentsystem.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class Company extends Audit {
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
    private String companyIntroduction;
    private String companyImage;
    private String companyAddress;
//    private String industry;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "industry_id")
    private Industry industry;
    private Integer companyFoundedYear;
    private String companyMst;
    private String companyLicense;
    private Integer companySize;
    private String companyTag;
    private boolean isActive;
    private String email;
    private String phoneNumber;
    private String website;
    private String facebookUrl;
    private String youtubeUrl;
    private String linkedinUrl;

//    @OneToMany(mappedBy = "company")
//    private Set<CompanyBranch> branches;

//    @OneToMany(mappedBy = "company")
//    Set<CompanyBenefit> benefitDetail;

    public Company(Company company, boolean deleteFlag) {
        super(company.getCreatedAt(),
                company.getUpdatedAt(),
                company.getCreatedBy(),
                company.getUpdatedBy(),
                deleteFlag,
                company.getCompanyId());
        this.companyShortName = company.getCompanyShortName();
        this.companyFullName = company.getCompanyFullName();
        this.companyLogo = company.getCompanyLogo();
        this.companyIntroduction = company.getCompanyIntroduction();
        this.companyImage = company.getCompanyImage();
        this.companyAddress = company.getCompanyAddress();
        this.industry = company.getIndustry();
        this.companyFoundedYear = company.getCompanyFoundedYear();
        this.companyMst = company.getCompanyMst();
        this.companyLicense = company.getCompanyLicense();
        this.companySize = company.getCompanySize();
        this.companyTag = company.getCompanyTag();
        this.isActive = company.isActive();
        this.email = company.getEmail();
        this.phoneNumber = company.getPhoneNumber();
        this.website = company.getWebsite();
        this.facebookUrl = company.getFacebookUrl();
        this.youtubeUrl = company.getYoutubeUrl();
        this.linkedinUrl = company.getLinkedinUrl();
        this.companyUrl = company.getCompanyUrl();
//        this.benefitDetail = company.getBenefitDetail();
    }
}
