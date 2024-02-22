package hust.seee.recruitmentsystem.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CandidateDTO {
    private Integer id;
    private Integer accountId;
    private String email;
    private String fullName;
    private String phoneNumber;
    private AddressDTO address;
    private String gender;
    private LocalDate birthday;
    private String imgUrl;
    private LocalDateTime createDate;
    private LocalDateTime lastModified;
    private String roleName;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<EducationDTO> education;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<HistoryDTO> workingHistory;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<SkillDTO> skill;
    private String desiredJob;
    private String educationLevel;
    private CategoryDTO category;
    private Integer categoryId;
    private String cvUrl;
    private String password;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String currentPassword;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String newPassword;

    private String provinceCode;
    private String key;
    private Integer page;
    private Integer size;
}
