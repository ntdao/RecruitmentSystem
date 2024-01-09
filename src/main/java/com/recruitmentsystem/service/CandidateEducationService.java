package com.recruitmentsystem.service;

import com.recruitmentsystem.dto.CandidateEducationDto;
import com.recruitmentsystem.entity.CandidateEducation;
import com.recruitmentsystem.mapper.CandidateEducationMapper;
import com.recruitmentsystem.repository.CandidateEducationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CandidateEducationService {
    private final CandidateEducationMapper candidateEducationMapper;
    private final CandidateEducationRepository candidateEducationRepository;

    public Set<CandidateEducation> addCandidateEducation(Set<CandidateEducation> list, CandidateEducationDto candidateEducationDto) {
        CandidateEducation candidateEducation = candidateEducationMapper.dtoToEntity(candidateEducationDto);
        list.add(candidateEducation);
        return list;
    }

    public Set<CandidateEducationDto> findByCandidate(Integer candidateId) {
        return candidateEducationRepository.findCandidateEducationByCandidate(candidateId)
                .stream()
                .map(candidateEducationMapper::entityToDto)
                .collect(Collectors.toSet());
    }

    public Set<CandidateEducation> deleteCandidateEducation(Integer educationId, Set<CandidateEducation> educations) {
        educations.forEach(e -> {
            if (e.getCandidateEducationId() == educationId) {
                educations.remove(e);
                candidateEducationRepository.deleteById(educationId);
            }
        });
        return educations;
    }

    public Set<CandidateEducation> updateCandidateEducation(Integer educationId, CandidateEducationDto dto, Set<CandidateEducation> educations) {
//        for (int i = 0; i < educations.size(); i++) {
//            if (educations.(i).getCandidateEducationId() == educationId) {
//                educations.remove(i);
//                CandidateEducation edu = candidateEducationMapper.dtoToEntity(dto);
//                edu.setCandidateEducationId(educationId);
//                educations.add(i, edu);
//            }
//        }
        return educations;
    }
}
