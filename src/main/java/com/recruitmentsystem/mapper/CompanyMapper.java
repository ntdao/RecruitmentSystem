package com.recruitmentsystem.mapper;

import com.recruitmentsystem.entity.Company;
import com.recruitmentsystem.model.company.CompanyDisplayModel;
import com.recruitmentsystem.model.company.CompanyRequestModel;
import org.springframework.stereotype.Service;

@Service
public class CompanyMapper {
    public CompanyDisplayModel companyToDisplayModel(Company company) {
        return CompanyDisplayModel
                .builder()
                .companyName(company.getCompanyName())
                .companyLogo(company.getCompanyLogo())
                .companyImage(company.getCompanyImage())
                .companySlogan(company.getCompanySlogan())
                .companyIntroduction(company.getCompanyIntroduction())
                .companyUrl(company.getCompanyUrl())
                .companyField(company.getCompanyField())
                .createdAt(company.getCreatedAt())
                .updatedAt(company.getUpdatedAt())
                .build();
    }

    public Company companyRequestModelToCompany(CompanyRequestModel request) {
        return Company
                .builder()
                .companyName(request.companyName())
                .companyLogo(request.companyLogo())
                .companyImage(request.companyImage())
                .companyIntroduction(request.companyIntroduction())
                .companySlogan(request.companySlogan())
                .companyUrl(request.companyUrl())
                .companyField(request.companyField())
                .companyName(request.companyName())
                .build();
    }
}
