package com.recruitmentsystem.mapper;

import com.recruitmentsystem.dto.UserDTO;
import com.recruitmentsystem.entity.User;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class UserDTOMapper implements Function<User, UserDTO> {
    @Override
    public UserDTO apply(User user) {
        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getAddress(),
                user.getGender(),
                user.getBirthday(),
                user.getImgUrl(),
                user.getRole(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.isDeleteFlag()
        );
    }
}
