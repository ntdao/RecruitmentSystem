package com.recruitmentsystem.dto;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record UserRequestModel(
        String password,
        String email,
        String fullName,
        String phoneNumber,
        AddressDto address,
        String gender,
        LocalDate birthday,
        String imgUrl,
        String roleName,
        String desiredJob,
        String cvUrl,
        String educationLevel,
        String workingHistory,
        Integer category
) {
}
