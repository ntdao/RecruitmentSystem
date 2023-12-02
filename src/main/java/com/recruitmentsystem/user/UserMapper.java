package com.recruitmentsystem.user;

import com.recruitmentsystem.address.address.AddressMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserMapper {
    private final AddressMapper addressMapper;
    public UserResponseModel userToResponseModel(User user) {
        return UserResponseModel
                .builder()
                .id(user.getUserId())
                .email(user.getAccount().getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .gender(user.getGender())
                .birthday(user.getBirthday())
                .imgUrl(user.getImgUrl())
                .createDate(user.getCreateDate())
                .lastModified(user.getLastModified())
                .roleName(user.getAccount().getAuthorities().toString())
                .address(addressMapper.addressToResponseModel(user.getAddress()))
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
                    .gender(request.gender())
                    .birthday(request.birthday())
                    .imgUrl("img/user_profile/avatar.png")
                    .build();
        }
    }
}
