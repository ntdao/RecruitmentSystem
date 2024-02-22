package hust.seee.recruitmentsystem.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import hust.seee.recruitmentsystem.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JobDTO {
    private Integer jobId;
    private String jobName;
    private Integer jobTypeId;
    private String jobTypeText;
    private Integer categoryId;
    private String categoryName;
    private String minEducationLevel;
    private String jobExperience;
    private Gender jobGender;
    private Integer jobQuantity;
    private LocalDateTime jobExpiredDate;
    private String jobSalary;
    private String jobDescription;
    private String jobRequirement;
    private String jobBenefit;
    private List<String> jobSkills;
    private AddressDTO jobAddress;
    private String companyName;
    private String companyLogo;
    private JobTypeDTO jobType;
    private Integer jobStatus;
    private LocalDateTime createDate;
    private CategoryDTO category;
    private List<SkillDTO> jobSkill;
    private Integer jobCandidate;
    private Integer jobPass;
    private String provinceCode;
    private Integer companyId;
    private Integer size;
    private Integer page;

    public JobDTO(String jobName, String companyName, String companyLogo, String jobSalary) {
        this.jobName = jobName;
        this.jobSalary = jobSalary;
        this.companyName = companyName;
        this.companyLogo = companyLogo;
    }
}
