package com.recruitmentsystem.user;

import lombok.Builder;

@Builder
public record ChangePasswordRequestModel(
        String currentPassword,
        String newPassword,
        String confirmationPassword
) {
}
