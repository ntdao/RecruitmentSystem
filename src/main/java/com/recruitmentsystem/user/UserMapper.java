package com.recruitmentsystem.user;

import com.recruitmentsystem.role.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserMapper {
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    public UserResponseModel userToResponseModel(User user) {
        return UserResponseModel
                .builder()
//                .id(user.getUserId())
//                .username(user.getUsername())
                .email(user.getAccount().getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .address(user.getAddress())
                .gender(user.getGender())
                .birthday(user.getBirthday())
                .imgUrl(user.getImgUrl())
                .createDate(user.getCreateDate())
                .lastModified(user.getLastModified())
                .roleName(user.getAccount().getAuthorities().toString())
                .build();
    }

    public User userRequestModelToUser(UserRequestModel request) {
        if (request.address() == null
                && request.password() == null
                && request.imgUrl() == null
                && request.roleName() == null) {
            return User
                    .builder()
//                    .username(request.username())
                    .firstName(request.firstName())
                    .lastName(request.lastName())
//                    .email(request.email())
                    .phoneNumber(request.phoneNumber())
                    .gender(request.gender())
                    .birthday(request.birthday())
                    .build();
        } else {
            return User
                    .builder()
//                    .username(request.username())
                    .firstName(request.firstName())
                    .lastName(request.lastName())
//                    .email(request.email())
//                    .password(passwordEncoder.encode(request.password()))
                    .phoneNumber(request.phoneNumber())
                    .address(request.address())
                    .gender(request.gender())
                    .birthday(request.birthday())
                    .imgUrl("img/user_profile/avatar.png")
//                    .role(roleService.findRoleByName(request.roleName()))
                    .build();
        }
    }
//    public User registerRequestModelToUser(RegisterRequestModel request) {
//        return User
//                .builder()
//                .username(request.username())
//                .firstName(request.firstName())
//                .lastName(request.lastName())
//                .email(request.email())
//                .password(passwordEncoder.encode(request.password()))
//                .phoneNumber(request.phoneNumber())
//                .gender(request.gender())
//                .birthday(request.birthday())
//                .imgUrl("img/user_profile/avatar.png")
//                .role(roleService.findRoleByName("CANDIDATE"))
//                .build();
//    }
}
