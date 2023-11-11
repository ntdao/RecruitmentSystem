package com.recruitmentsystem.auth;

import com.recruitmentsystem.common.myEnum.Gender;

import java.time.LocalDate;

public record RegisterRequestModel (
    String username,
    String firstName,
    String lastName,
    String email,
    String password,
    String phoneNumber,
    Gender gender,
    LocalDate birthday
){}
