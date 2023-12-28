package com.recruitmentsystem.mapper;

import com.recruitmentsystem.service.DegreeService;
import com.recruitmentsystem.entity.UserEducation;
import com.recruitmentsystem.dto.UserEducationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserEducationMapper {
    private final DegreeService degreeService;
    public UserEducationDto entityToDto(UserEducation userEducation) {
        return UserEducationDto.builder()
                .schoolName(userEducation.getSchoolName())
                .major(userEducation.getMajor())
                .startDate(userEducation.getStartDate())
                .endDate(userEducation.getEndDate())
                .degreeName(userEducation.getDegree().getDegreeNameVI())
                .description(userEducation.getDescription())
                .build();
    }

    public Set<UserEducationDto> entitiesToDtos(Set<UserEducation> educations) {
        return educations.stream()
                .map(this::entityToDto)
                .collect(Collectors.toSet());
    }

    public UserEducation dtoToEntity(UserEducationDto userEducationDto) {
        return UserEducation.builder()
                .schoolName(userEducationDto.schoolName())
                .major(userEducationDto.major())
                .startDate(userEducationDto.startDate())
                .endDate(userEducationDto.endDate())
                .degree(degreeService.findDegreeByName(userEducationDto.degreeName()))
                .build();
    }
}
