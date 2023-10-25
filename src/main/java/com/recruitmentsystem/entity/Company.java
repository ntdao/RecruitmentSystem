package com.recruitmentsystem.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Data
@Table
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@ToString
public class Company extends Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Integer companyId;
    @Column(nullable = false)
    private String companyName;
    private String companyLogo;
    private String companyUrl;
    private String companyIntroduction;
    private String companySlogan;
    private String companyImage;
    private String companyAddress;
    private String companyField;
//    private boolean isActive;

//    @OneToMany(mappedBy = "company")
//    private Set<CompanyBranch> branches;

    public Company(Company company, boolean deleteFlag) {
        super(company.getCreatedAt(),
                company.getUpdatedAt(),
                company.getCreatedBy(),
                company.getUpdatedBy(),
                deleteFlag,
                company.getCompanyId());
        this.companyName = company.getCompanyName();
        this.companyLogo = company.getCompanyLogo();
        this.companyIntroduction = company.getCompanyIntroduction();
        this.companyImage = company.getCompanyImage();
        this.companySlogan = company.getCompanySlogan();
        this.companyAddress = company.getCompanyAddress();
        this.companyUrl = company.getCompanyUrl();
        this.companyField = company.getCompanyField();
//        this.isActive = company.isActive();
    }
}
