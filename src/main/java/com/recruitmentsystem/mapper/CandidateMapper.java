package com.recruitmentsystem.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recruitmentsystem.dto.*;
import com.recruitmentsystem.entity.Candidate;
import com.recruitmentsystem.enums.Gender;
import com.recruitmentsystem.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CandidateMapper {
    private final AddressMapper addressMapper;
    private final ObjectMapper objectMapper;
    private final CandidateEducationMapper educationMapper;
    private final CategoryService categoryService;

    public CandidateResponseModel entityToDto(Candidate candidate) {
        AddressDto address = addressMapper.entityToDto(candidate.getAddress());
        Set<CandidateEducationDto> educations = educationMapper.entitiesToDtos(candidate.getCandidateEducations());
        List<CandidateWorkingHistoryDto> histories = candidate.getWorkingHistories()
                .stream()
                .map(h -> objectMapper.convertValue(h, CandidateWorkingHistoryDto.class))
                .toList();
        List<SkillDTO> skills = candidate.getCandidateSkills()
                .stream()
                .map(s -> objectMapper.convertValue(s, SkillDTO.class))
                .toList();
        CategoryDTO category = objectMapper.convertValue(candidate.getCategory(), CategoryDTO.class);

        return CandidateResponseModel
                .builder()
                .id(candidate.getCandidateId())
                .accountId(candidate.getAccount().getId())
                .email(candidate.getAccount().getUsername())
                .fullName(candidate.getFullName())
                .phoneNumber(candidate.getPhoneNumber())
                .gender(candidate.getGender().toString())
                .birthday(candidate.getBirthday())
                .imgUrl(candidate.getImgUrl())
                .createDate(candidate.getCreateDate())
                .lastModified(candidate.getLastModified())
                .roleName(candidate.getAccount().getAuthorities().toString())
                .address(address)
                .education(educations.stream().toList())
                .workingHistory(histories)
                .skill(skills)
                .desiredJob(candidate.getDesiredJob())
                .category(category)
                .educationLevel(candidate.getEducationLevel())
                .cvUrl(candidate.getCvUrl())
                .build();
    }

    public Candidate dtoToEntity(CandidateRequestModel request) {
        if (request.password() == null
                && request.imgUrl() == null
                && request.roleName() == null) {
            return Candidate
                    .builder()
                    .fullName(request.fullName())
                    .phoneNumber(request.phoneNumber())
                    .gender(Gender.valueOf(request.gender()))
                    .birthday(request.birthday())
                    .desiredJob(request.desiredJob())
                    .educationLevel(request.educationLevel())
                    .category(categoryService.findById(request.category()))
                    .build();
        } else {
            return Candidate
                    .builder()
                    .fullName(request.fullName())
                    .phoneNumber(request.phoneNumber())
                    .gender(Gender.valueOf(request.gender()))
                    .birthday(request.birthday())
                    .imgUrl(request.imgUrl())
                    .desiredJob(request.desiredJob())
                    .educationLevel(request.educationLevel())
                    .category(categoryService.findById(request.category()))
                    .build();
        }
    }
}
