package com.recruitmentsystem.model.user;

import com.recruitmentsystem.common.myEnum.Gender;
import lombok.Builder;

import java.time.Instant;
import java.time.LocalDate;

@Builder
public record UserDisplayModel(
        Integer id,
        String username,
        String email,
        String firstName,
        String lastName,
        String phoneNumber,
        String address,
        Gender gender,
        LocalDate birthday,
        String imgUrl,
        Instant createdAt,
        Instant updatedAt,
        String roleName
) {
}
