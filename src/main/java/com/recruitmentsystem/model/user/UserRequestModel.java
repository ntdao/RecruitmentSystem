package com.recruitmentsystem.model.user;

import com.recruitmentsystem.common.myEnum.Gender;

import java.time.LocalDate;

public record UserRequestModel(
        String username,
        String password,
        String email,
        String firstName,
        String lastName,
        String phoneNumber,
        String address,
        Gender gender,
        LocalDate birthday,
        String imgUrl,
        String roleName,
        LocalDate createdAt,
        LocalDate updatedAt
){
}
