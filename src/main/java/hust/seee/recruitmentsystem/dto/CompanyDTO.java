package hust.seee.recruitmentsystem.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CompanyDTO {
    private Integer companyId;
    private Integer accountId;
    private String password;
    private String companyShortName;
    private String companyFullName;
    private AddressDTO companyAddress;
    private String companyLogo;
    private String companyIntroduction;
    private Integer companyIndustryId;
    private IndustryDTO companyIndustry;
    private String companyImage;
    private Integer companyFoundedYear;
    private String companyMST;
    private String companyLicense;
    private String companySize;
    private String email;
    private String phoneNumber;
    private String website;
    private String facebookUrl;
    private String youtubeUrl;
    private String linkedinUrl;
    private String companyBranch;
    private LocalDateTime createDate;
    private LocalDateTime lastModified;
    private String provinceCode;
    private Integer page;
    private Integer size;
}
