package com.recruitmentsystem.company;

import com.recruitmentsystem.address.address.AddressMapper;
import com.recruitmentsystem.industry.IndustryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyMapper {
    private final AddressMapper addressMapper;
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
                .industry(company.getIndustry().getIndustryId())
                .companyAddress(addressMapper.addressToResponseModel(company.getAddress()))
                .companyMST(company.getCompanyMst())
                .companyLicense(company.getCompanyLicense())
                .companySize(company.getCompanySize())
                .companyFoundedYear(company.getCompanyFoundedYear())
                .companyTag(company.getCompanyTag())
                .email(company.getAccount().getEmail())
                .phoneNumber(company.getPhoneNumber())
                .website(company.getWebsite())
                .facebookUrl(company.getFacebookUrl())
                .youtubeUrl(company.getYoutubeUrl())
                .linkedinUrl(company.getLinkedinUrl())
                .createDate(company.getCreateDate())
                .lastModified(company.getLastModified())
                .companyBranch(company.getCompanyBranch())
                .build();
    }

    public Company companyRequestModelToCompany(CompanyRequestModel request) {
        return Company
                .builder()
                .companyShortName(request.companyShortName())
                .companyFullName(request.companyFullName())
                .companyIntroduction(request.companyIntroduction())
                .companyUrl(request.companyUrl())
                .industry(industryService.findById(request.industryId()))
                .companyMst(request.companyMST())
                .companySize(request.companySize())
                .companyFoundedYear(request.companyFoundedYear())
                .companyTag(request.companyTag())
                .companyBranch(request.companyBranch())
                .phoneNumber(request.phoneNumber())
                .website(request.website())
                .facebookUrl(request.facebookUrl())
                .youtubeUrl(request.youtubeUrl())
                .linkedinUrl(request.linkedinUrl())
                .companyLogo(request.companyLogo())
                .companyLicense(request.companyLicense())
                .companyImage(request.companyImage())
                .build();
    }
}
