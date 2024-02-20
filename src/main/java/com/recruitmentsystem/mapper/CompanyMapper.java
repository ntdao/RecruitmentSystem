package com.recruitmentsystem.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recruitmentsystem.dto.CompanyDto;
import com.recruitmentsystem.dto.CompanyResponseModel;
import com.recruitmentsystem.dto.IndustryDTO;
import com.recruitmentsystem.entity.Company;
import com.recruitmentsystem.service.IndustryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyMapper {
    private final AddressMapper addressMapper;
    private final ObjectMapper objectMapper;
    private final IndustryService industryService;

    public CompanyResponseModel entityToDto(Company company) {
        return CompanyResponseModel
                .builder()
                .companyId(company.getCompanyId())
                .accountId(company.getAccount().getId())
                .companyShortName(company.getCompanyShortName())
                .companyFullName(company.getCompanyFullName())
                .companyLogo(company.getCompanyLogo())
                .companyImage(company.getCompanyImage())
                .companyIntroduction(company.getCompanyIntroduction())
                .companyIndustry(objectMapper.convertValue(company.getIndustry(), IndustryDTO.class))
                .companyAddress(addressMapper.entityToDto(company.getCompanyAddress()))
                .companyMST(company.getCompanyMst())
                .companyLicense(company.getCompanyLicense())
                .companySize(company.getCompanySize())
                .companyFoundedYear(company.getCompanyFoundedYear())
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

    public Company dtoToEntity(CompanyDto request) {
        return Company
                .builder()
                .companyShortName(request.companyShortName())
                .companyFullName(request.companyFullName())
                .companyIntroduction(request.companyIntroduction())
                .industry(industryService.findById(request.companyIndustryId()))
                .companyMst(request.companyMST())
                .companySize(request.companySize())
                .companyFoundedYear(request.companyFoundedYear())
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
