package com.recruitmentsystem.dto;

import com.recruitmentsystem.entity.Role;
import com.recruitmentsystem.myEnum.Gender;

import java.time.LocalDate;

public record UserDTO(
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
        Role role,
        LocalDate createdAt,
        LocalDate updatedAt,
        boolean deleteFlag
) {
}
