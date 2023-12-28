package com.recruitmentsystem.dto;

import lombok.Builder;

@Builder
public record WardDto(
        String wardCode,
        String fullName
) {
}
