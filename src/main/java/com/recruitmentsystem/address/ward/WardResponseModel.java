package com.recruitmentsystem.address.ward;

import lombok.Builder;

@Builder
public record WardResponseModel(
        String code,
        String fullName
) {
}
