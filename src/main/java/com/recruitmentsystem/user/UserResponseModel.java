package com.recruitmentsystem.user;

import com.recruitmentsystem.address.address.AddressResponseModel;
import com.recruitmentsystem.common.myEnum.Gender;
import com.recruitmentsystem.usereducation.UserEducationDto;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Builder
public record UserResponseModel(
        Integer id,
        String email,
        String fullName,
        String phoneNumber,
        AddressResponseModel address,
        String gender,
        LocalDate birthday,
        String imgUrl,
        LocalDateTime createDate,
        LocalDateTime lastModified,
        String roleName,
        List<UserEducationDto> education
) {
}
