package com.recruitmentsystem.mapper;

import com.recruitmentsystem.entity.User;
import com.recruitmentsystem.model.user.UserDisplayModel;
import com.recruitmentsystem.model.user.UserRequestModel;
import com.recruitmentsystem.service.IRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserMapper {
    private final PasswordEncoder passwordEncoder;
    private final IRoleService roleService;

    public UserDisplayModel userToDisplayModel(User user) {
        return UserDisplayModel
                .builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .address(user.getAddress())
                .gender(user.getGender())
                .birthday(user.getBirthday())
                .imgUrl(user.getImgUrl())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .roleName(user.getRole().getRoleName())
                .build();
    }

    public User userRequestModelToUser(UserRequestModel request) {
        if (request.address() == null && request.imgUrl() == null) {
            return User
                    .builder()
                    .username(request.username())
                    .firstName(request.firstName())
                    .lastName(request.lastName())
                    .email(request.email())
                    .password(passwordEncoder.encode(request.password()))
                    .phoneNumber(request.phoneNumber())
                    .gender(request.gender())
                    .birthday(request.birthday())
                    .role(roleService.findByRoleName("ROLE_USER"))
                    .build();
        } else {
            return User
                    .builder()
                    .username(request.username())
                    .firstName(request.firstName())
                    .lastName(request.lastName())
                    .email(request.email())
                    .password(passwordEncoder.encode(request.password()))
                    .phoneNumber(request.phoneNumber())
                    .address(request.address())
                    .gender(request.gender())
                    .birthday(request.birthday())
                    .imgUrl(request.imgUrl())
                    .role(roleService.findByRoleName(request.roleName()))
                    .build();
        }
    }
}
