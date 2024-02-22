package hust.seee.recruitmentsystem.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import hust.seee.recruitmentsystem.dto.CompanyDTO;
import hust.seee.recruitmentsystem.dto.IndustryDTO;
import hust.seee.recruitmentsystem.entity.Company;
import hust.seee.recruitmentsystem.service.IndustryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyMapper {
    private final AddressMapper addressMapper;
    private final ObjectMapper objectMapper;
    private final IndustryService industryService;

    public CompanyDTO entityToDto(Company company) {
        return CompanyDTO
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

    public Company dtoToEntity(CompanyDTO request) {
        return Company
                .builder()
                .companyShortName(request.getCompanyShortName())
                .companyFullName(request.getCompanyFullName())
                .companyIntroduction(request.getCompanyIntroduction())
                .industry(industryService.findById(request.getCompanyIndustryId()))
                .companyMst(request.getCompanyMST())
                .companySize(request.getCompanySize())
                .companyFoundedYear(request.getCompanyFoundedYear())
                .companyBranch(request.getCompanyBranch())
                .phoneNumber(request.getPhoneNumber())
                .website(request.getWebsite())
                .facebookUrl(request.getFacebookUrl())
                .youtubeUrl(request.getYoutubeUrl())
                .linkedinUrl(request.getLinkedinUrl())
                .companyLogo(request.getCompanyLogo())
                .companyLicense(request.getCompanyLicense())
                .companyImage(request.getCompanyImage())
                .build();
    }
}
