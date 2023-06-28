package com.recruitmentsystem.model.user;

import com.recruitmentsystem.common.myEnum.Gender;
import jakarta.annotation.Nullable;

import java.time.Instant;
import java.time.LocalDate;

public record UserRequestModel(
        String username,
        String password,
        String email,
        String firstName,
        String lastName,
        String phoneNumber,
        @Nullable String address,
        Gender gender,
        LocalDate birthday,
        @Nullable String imgUrl,
        String roleName,
        @Nullable Instant createdAt,
        @Nullable Instant updatedAt,
        String token
) {
}
