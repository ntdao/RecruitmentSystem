package com.recruitmentsystem.model.user;

import lombok.Builder;

@Builder
public record UserChangePassword(
        String currentPassword,
        String newPassword

) {
}
