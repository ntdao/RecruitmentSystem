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
                .id(company.getCompanyId())
                .name(company.getCompanyName())
                .imgUrl(company.getCompanyImageUrl())
                .createdAt(company.getCreatedAt())
                .updatedAt(company.getUpdatedAt())
                .build();
    }

    public Company companyRequestModelToCompany(CompanyRequestModel request) {
        return Company
                .builder()
                .companyName(request.name())
                .companyImageUrl(request.imgUrl())
                .build();
    }
}
