package com.recruitmentsystem.user;

import com.recruitmentsystem.address.address.Address;
import com.recruitmentsystem.address.address.AddressResponseModel;
import com.recruitmentsystem.common.myEnum.Gender;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
public record UserResponseModel(
//        String username,
        Integer id,
        String email,
        String firstName,
        String lastName,
        String phoneNumber,
        AddressResponseModel address,
        Gender gender,
        LocalDate birthday,
        String imgUrl,
        LocalDateTime createDate,
        LocalDateTime lastModified,
        String roleName
) {
}
