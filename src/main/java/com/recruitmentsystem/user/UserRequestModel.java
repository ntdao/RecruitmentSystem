package com.recruitmentsystem.user;

import com.recruitmentsystem.address.address.AddressRequestModel;
import com.recruitmentsystem.common.myEnum.Gender;
import jakarta.annotation.Nullable;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record UserRequestModel(
        @Nullable String password,
        String email,
        String fullName,
        String phoneNumber,
        @Nullable AddressRequestModel address,
        Gender gender,
        LocalDate birthday,
        @Nullable String imgUrl,
        String roleName
) {
}
