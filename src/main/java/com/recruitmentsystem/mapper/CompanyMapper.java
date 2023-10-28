package com.recruitmentsystem.mapper;

import com.recruitmentsystem.entity.Company;
import com.recruitmentsystem.model.company.CompanyDisplayModel;
import com.recruitmentsystem.model.company.CompanyRequestModel;
import com.recruitmentsystem.service.BranchService;
import com.recruitmentsystem.service.IndustryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyMapper {
    private final BranchService branchService;
    private final IndustryService industryService;

    public CompanyDisplayModel companyToDisplayModel(Company company) {
        return CompanyDisplayModel
                .builder()
                .id(company.getCompanyId())
                .companyShortName(company.getCompanyShortName())
                .companyFullName(company.getCompanyFullName())
                .companyLogo(company.getCompanyLogo())
                .companyImage(company.getCompanyImage())
                .companyIntroduction(company.getCompanyIntroduction())
                .companyUrl(company.getCompanyUrl())
                .industry(company.getIndustry().getIndustryNameVn())
                .companyAddress(company.getCompanyAddress())
                .companyMST(company.getCompanyMst())
                .companyLicense(company.getCompanyLicense())
                .companySize(company.getCompanySize())
                .companyFoundedYear(company.getCompanyFoundedYear())
                .companyTag(company.getCompanyTag())
                .branches(branchService.findAllBranchesAddressByCompany(company.getCompanyId()))
                .isActive(company.isActive())
                .email(company.getEmail())
                .phoneNumber(company.getPhoneNumber())
                .website(company.getWebsite())
                .facebookUrl(company.getFacebookUrl())
                .youtubeUrl(company.getYoutubeUrl())
                .linkedinUrl(company.getLinkedinUrl())
                .createdAt(company.getCreatedAt())
                .updatedAt(company.getUpdatedAt())
                .build();
    }

    public Company companyRequestModelToCompany(CompanyRequestModel request) {
        return Company
                .builder()
                .companyShortName(request.companyShortName())
                .companyFullName(request.companyFullName())
                .companyLogo(request.companyLogo())
                .companyImage(request.companyImage())
                .companyIntroduction(request.companyIntroduction())
                .companyUrl(request.companyUrl())
                .industry(industryService.findByName(request.industry()))
                .companyAddress(request.companyAddress())
                .companyMst(request.companyMST())
                .companyLicense(request.companyLicense())
                .companySize(request.companySize())
                .companyFoundedYear(request.companyFoundedYear())
                .companyTag(request.companyTag())
//                .isActive(request.isActive())
                .email(request.email())
                .phoneNumber(request.phoneNumber())
                .website(request.website())
                .facebookUrl(request.facebookUrl())
                .youtubeUrl(request.youtubeUrl())
                .linkedinUrl(request.linkedinUrl())
                .build();
    }
}
