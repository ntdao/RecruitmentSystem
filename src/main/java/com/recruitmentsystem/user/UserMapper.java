package com.recruitmentsystem.user;

import org.springframework.stereotype.Service;

@Service
public class UserMapper {
    public UserResponseModel userToResponseModel(User user) {
        return UserResponseModel
                .builder()
                .id(user.getUserId())
                .email(user.getAccount().getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
//                .address(addressMapper.addressToResponseModel(user.getAddress()))
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
                    .firstName(request.firstName())
                    .lastName(request.lastName())
//                    .address(addressService.addressRequestModelToEntity(request.address()))
                    .phoneNumber(request.phoneNumber())
                    .gender(request.gender())
                    .birthday(request.birthday())
                    .build();
        } else {
            return User
                    .builder()
                    .firstName(request.firstName())
                    .lastName(request.lastName())
                    .phoneNumber(request.phoneNumber())
//                    .address(addressService.addressRequestModelToEntity(request.address()))
                    .gender(request.gender())
                    .birthday(request.birthday())
                    .imgUrl("img/user_profile/avatar.png")
                    .build();
        }
    }
}
