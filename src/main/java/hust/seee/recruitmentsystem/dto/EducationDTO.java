package hust.seee.recruitmentsystem.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EducationDTO {
    private Integer id;
    private String schoolName;
    private String major;
    private String description;
    private String startDate;
    private String endDate;
    private String degreeName;
}
