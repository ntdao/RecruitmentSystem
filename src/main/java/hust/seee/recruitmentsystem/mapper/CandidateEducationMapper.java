package hust.seee.recruitmentsystem.mapper;

import hust.seee.recruitmentsystem.dto.EducationDTO;
import hust.seee.recruitmentsystem.entity.CandidateEducation;
import hust.seee.recruitmentsystem.service.DegreeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CandidateEducationMapper {
    private final DegreeService degreeService;

    public EducationDTO entityToDto(CandidateEducation candidateEducation) {
        return EducationDTO.builder()
                .id(candidateEducation.getCandidateEducationId())
                .schoolName(candidateEducation.getSchoolName())
                .major(candidateEducation.getMajor())
                .startDate(candidateEducation.getStartDate())
                .endDate(candidateEducation.getEndDate())
                .degreeName(candidateEducation.getDegree().getName())
                .description(candidateEducation.getDescription())
                .build();
    }

    public Set<EducationDTO> entitiesToDtos(Set<CandidateEducation> educations) {
        return educations.stream()
                .map(this::entityToDto)
                .collect(Collectors.toSet());
    }

    public CandidateEducation dtoToEntity(EducationDTO dto) {
        return CandidateEducation.builder()
                .schoolName(dto.getSchoolName())
                .major(dto.getMajor())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .degree(degreeService.findByName(dto.getDegreeName()))
                .build();
    }
}
