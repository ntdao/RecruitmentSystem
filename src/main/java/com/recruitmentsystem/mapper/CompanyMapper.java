package com.recruitmentsystem.mapper;

import com.recruitmentsystem.entity.Company;
import com.recruitmentsystem.model.company.CompanyDisplayModel;
import com.recruitmentsystem.model.company.CompanyRequestModel;
import com.recruitmentsystem.service.BranchService;
import org.springframework.stereotype.Service;

@Service
public class CompanyMapper {
    private final BranchService branchService;

    public CompanyMapper(BranchService branchService) {
        this.branchService = branchService;
    }

    public CompanyDisplayModel companyToDisplayModel(Company company) {
        return CompanyDisplayModel
                .builder()
                .companyId(company.getCompanyId())
                .companyName(company.getCompanyName())
                .companyLogo(company.getCompanyLogo())
                .companyImage(company.getCompanyImage())
                .companySlogan(company.getCompanySlogan())
                .companyIntroduction(company.getCompanyIntroduction())
                .companyUrl(company.getCompanyUrl())
                .companyField(company.getCompanyField())
                .branches(branchService.findAllBranchesByCompany(company.getCompanyId()))
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
