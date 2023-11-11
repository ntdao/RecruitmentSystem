package com.recruitmentsystem.company;

import com.recruitmentsystem.industry.IndustryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyMapper {
    private final IndustryService industryService;

    public CompanyResponseModel companyToResponseModel(Company company) {
        return CompanyResponseModel
                .builder()
                .id(company.getCompanyId())
                .companyShortName(company.getCompanyShortName())
                .companyFullName(company.getCompanyFullName())
                .companyLogo(company.getCompanyLogo())
                .companyImage(company.getCompanyImage())
                .companyIntroduction(company.getCompanyIntroduction())
                .companyUrl(company.getCompanyUrl())
                .industry(company.getIndustry().getIndustryNameVI())
                .companyAddress(company.getCompanyAddress())
                .companyMST(company.getCompanyMst())
                .companyLicense(company.getCompanyLicense())
                .companySize(company.getCompanySize())
                .companyFoundedYear(company.getCompanyFoundedYear())
                .companyTag(company.getCompanyTag())
                .isActive(company.isActive())
                .email(company.getAccount().getEmail())
                .phoneNumber(company.getPhoneNumber())
                .website(company.getWebsite())
                .facebookUrl(company.getFacebookUrl())
                .youtubeUrl(company.getYoutubeUrl())
                .linkedinUrl(company.getLinkedinUrl())
                .createDate(company.getCreateDate())
                .lastModified(company.getLastModified())
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
//                .email(request.email())
                .phoneNumber(request.phoneNumber())
                .website(request.website())
                .facebookUrl(request.facebookUrl())
                .youtubeUrl(request.youtubeUrl())
                .linkedinUrl(request.linkedinUrl())
                .build();
    }
}
