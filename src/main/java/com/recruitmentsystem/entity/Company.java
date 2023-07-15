package com.recruitmentsystem.entity;

import jakarta.persistence.*;
import lombok.*;

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

//    @Column(nullable = false)
//    private String companyInfo;

    private String companyMessage;

    private String companySlogan;

    private String companyImage;

    private String companyAddress;

    public Company(Company company, Integer oldId, boolean deleteFlag) {
        super(company.getCreatedAt(),
                company.getUpdatedAt(),
                company.getCreatedBy(),
                company.getUpdatedBy(),
                deleteFlag,
                oldId);
        this.companyName = company.getCompanyName();
        this.companyLogo = company.getCompanyLogo();
        this.companyMessage = company.getCompanyMessage();
        this.companyImage = company.getCompanyImage();
        this.companySlogan = company.getCompanySlogan();
        this.companyAddress = company.getCompanyAddress();
    }
}
