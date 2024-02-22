package hust.seee.recruitmentsystem.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RecruitmentDTO {
    private Integer applicationId;
    private Integer candidateId;
    private String candidateFullName;
    private String candidateImgUrl;
    private LocalDate candidateBirthday;
    private String candidateAddress;
    private Integer jobId;
    private String jobName;
    private String companyName;
    private String companyLogo;
    private String jobQuantity;
    private String jobCandidate;
    private String applicationTimeAgo;
    private Integer applicationStatus;
}
