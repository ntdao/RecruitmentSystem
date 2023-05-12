package com.recruitmentsystem.registration;

import com.recruitmentsystem.myEnum.Gender;

import java.time.LocalDate;

public record UserRegistrationRequest (
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
        String roleName
){
}
