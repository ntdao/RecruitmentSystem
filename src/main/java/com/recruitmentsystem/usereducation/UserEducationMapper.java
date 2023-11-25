package com.recruitmentsystem.usereducation;

import com.recruitmentsystem.degree.DegreeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
                .build();
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
