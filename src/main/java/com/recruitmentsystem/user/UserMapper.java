package com.recruitmentsystem.user;

import com.recruitmentsystem.address.address.AddressMapper;
import com.recruitmentsystem.usereducation.UserEducationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserMapper {
    private final AddressMapper addressMapper;
    private final UserEducationMapper educationMapper;
    public UserResponseModel userToResponseModel(User user) {
        return UserResponseModel
                .builder()
                .id(user.getUserId())
                .email(user.getAccount().getUsername())
                .fullName(user.getFullName())
                .phoneNumber(user.getPhoneNumber())
                .gender(user.getGender().toString())
                .birthday(user.getBirthday())
                .imgUrl(user.getImgUrl())
                .createDate(user.getCreateDate())
                .lastModified(user.getLastModified())
                .roleName(user.getAccount().getAuthorities().toString())
                .address(addressMapper.addressToResponseModel(user.getAddress()))
                .education(educationMapper.entitiesToDtos(user.getUserEducations()))
                .build();
    }

    public User userRequestModelToUser(UserRequestModel request) {
        if (request.address() == null
                && request.password() == null
                && request.imgUrl() == null
                && request.roleName() == null) {
            return User
                    .builder()
                    .fullName(request.fullName())
                    .phoneNumber(request.phoneNumber())
                    .gender(request.gender())
                    .birthday(request.birthday())
                    .build();
        } else {
            return User
                    .builder()
                    .fullName(request.fullName())
                    .phoneNumber(request.phoneNumber())
                    .gender(request.gender())
                    .birthday(request.birthday())
                    .imgUrl("img/user_profile/avatar.png")
                    .build();
        }
    }
}
