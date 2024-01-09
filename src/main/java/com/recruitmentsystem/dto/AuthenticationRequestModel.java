package com.recruitmentsystem.dto;

import lombok.Builder;

@Builder
public record AuthenticationRequestModel(
        String email,
        String password
) {
}
