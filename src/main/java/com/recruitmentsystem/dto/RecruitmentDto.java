package com.recruitmentsystem.dto;

public record RecruitmentDto(
        Integer applicationId,
        UserResponseModel candidate,
        JobResponseModel jobResponseModel,
        Integer status
) {
}
