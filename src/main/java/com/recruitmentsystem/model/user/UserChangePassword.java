package com.recruitmentsystem.model.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class UserChangePassword {
    private String currentPassword;
    private String newPassword;
}
