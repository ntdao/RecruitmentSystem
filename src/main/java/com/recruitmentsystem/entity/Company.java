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
    private  String companyName;
    private String companyImageUrl;

    @Column(nullable = false)
    private String companyInfo;

    public Company(Company company, Integer oldId, boolean deleteFlag) {
        super(company.getCreatedAt(),
                company.getUpdatedAt(),
                company.getCreatedBy(),
                company.getUpdatedBy(),
                deleteFlag,
                oldId);
        this.companyName = company.getCompanyName();
        this.companyImageUrl = company.getCompanyImageUrl();
        this.companyInfo = company.getCompanyInfo();
    }
}
