package com.recruitmentsystem.user;

import com.recruitmentsystem.address.address.AddressResponseModel;
import com.recruitmentsystem.category.CategoryResponseModel;
import com.recruitmentsystem.usereducation.UserEducationDto;
import com.recruitmentsystem.userworkinghistory.UserWorkingHistoryDto;
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
        List<UserEducationDto> education,
        List<UserWorkingHistoryDto> workingHistory,
        String desiredJob,
        String educationLevel,
        CategoryResponseModel category,
        String cvUrl
) {
}
