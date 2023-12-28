package com.recruitmentsystem.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recruitmentsystem.dto.*;
import com.recruitmentsystem.entity.User;
import com.recruitmentsystem.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserMapper {
    private final AddressMapper addressMapper;
    private final ObjectMapper objectMapper;
    private final UserEducationMapper educationMapper;
    private final CategoryService categoryService;

    public UserResponseModel userToResponseModel(User user) {
        AddressDto addresses = addressMapper.addressToResponseModel(user.getAddress());
        Set<UserEducationDto> educations = educationMapper.entitiesToDtos(user.getUserEducations());
        List<UserWorkingHistoryDto> histories = user.getWorkingHistories()
                        .stream()
                        .map(h -> objectMapper.convertValue(h, UserWorkingHistoryDto.class))
                        .toList();
        List<SkillDto> skills = user.getUserSkills()
                .stream()
                .map(s -> objectMapper.convertValue(s, SkillDto.class))
                .toList();
        CategoryDto category = objectMapper.convertValue(user.getCategory(), CategoryDto.class);

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
                .address(addresses)
                .education(educations.stream().toList())
                .workingHistory(histories)
                .skill(skills)
                .desiredJob(user.getDesiredJob())
                .category(category)
                .educationLevel(user.getEducationLevel())
                .cvUrl(user.getCvUrl())
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
                    .desiredJob(request.desiredJob())
                    .educationLevel(request.educationLevel())
                    .category(categoryService.findById(request.category()))
                    .build();
        } else {
            return User
                    .builder()
                    .fullName(request.fullName())
                    .phoneNumber(request.phoneNumber())
                    .gender(request.gender())
                    .birthday(request.birthday())
                    .imgUrl(request.imgUrl())
                    .desiredJob(request.desiredJob())
                    .educationLevel(request.educationLevel())
                    .category(categoryService.findById(request.category()))
                    .build();
        }
    }
}
