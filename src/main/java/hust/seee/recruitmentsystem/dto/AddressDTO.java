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
public class AddressDTO {
    private Integer id;
    private String provinceCode;
    private String province;
    private String districtCode;
    private String wardCode;
    private String address;
    private String fullAddress;
}
