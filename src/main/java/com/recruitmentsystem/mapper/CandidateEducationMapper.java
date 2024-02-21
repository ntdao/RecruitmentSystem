package com.recruitmentsystem.mapper;

import com.recruitmentsystem.dto.CandidateEducationDto;
import com.recruitmentsystem.entity.CandidateEducation;
import com.recruitmentsystem.service.DegreeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CandidateEducationMapper {
    private final DegreeService degreeService;

    public CandidateEducationDto entityToDto(CandidateEducation candidateEducation) {
        return CandidateEducationDto.builder()
                .id(candidateEducation.getCandidateEducationId())
                .schoolName(candidateEducation.getSchoolName())
                .major(candidateEducation.getMajor())
                .startDate(candidateEducation.getStartDate())
                .endDate(candidateEducation.getEndDate())
                .degreeName(candidateEducation.getDegree().getName())
                .description(candidateEducation.getDescription())
                .build();
    }

    public Set<CandidateEducationDto> entitiesToDtos(Set<CandidateEducation> educations) {
        return educations.stream()
                .map(this::entityToDto)
                .collect(Collectors.toSet());
    }

    public CandidateEducation dtoToEntity(CandidateEducationDto candidateEducationDto) {
        return CandidateEducation.builder()
                .schoolName(candidateEducationDto.schoolName())
                .major(candidateEducationDto.major())
                .startDate(candidateEducationDto.startDate())
                .endDate(candidateEducationDto.endDate())
                .degree(degreeService.findByName(candidateEducationDto.degreeName()))
                .build();
    }
}
