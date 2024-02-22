package hust.seee.recruitmentsystem.service;

import hust.seee.recruitmentsystem.dto.EducationDTO;
import hust.seee.recruitmentsystem.entity.Candidate;
import hust.seee.recruitmentsystem.entity.CandidateEducation;
import hust.seee.recruitmentsystem.exception.ResourceNotFoundException;
import hust.seee.recruitmentsystem.mapper.CandidateEducationMapper;
import hust.seee.recruitmentsystem.repository.CandidateEducationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CandidateEducationService {
    private final CandidateEducationMapper educationMapper;
    private final CandidateEducationRepository educationRepository;

//    public Set<CandidateCandidateEducation> addCandidateCandidateEducation(Set<CandidateCandidateEducation> list, CandidateCandidateEducationDto dto) {
//        CandidateCandidateEducation candidateCandidateEducation = candidateCandidateEducationMapper.dtoToEntity(dto);
//        list.add(candidateCandidateEducation);
//        return list;
//    }

    public Set<EducationDTO> findByCandidate(Integer candidateId) {
        return educationRepository.findCandidateEducationByCandidate(candidateId)
                .stream()
                .map(educationMapper::entityToDto)
                .collect(Collectors.toSet());
    }

    public void delete(Integer educationId) {
//        educations.forEach(e -> {
//            if (Objects.equals(e.getCandidateCandidateEducationId(), educationId)) {
//                educations.remove(e);
//                candidateCandidateEducationRepository.deleteById(educationId);
//            }
//        });
//        return educations;
        educationRepository.deleteById(educationId);
    }

//    public Set<CandidateCandidateEducation> updateCandidateCandidateEducation(Integer educationId, CandidateCandidateEducationDto dto, Set<CandidateCandidateEducation> educations) {
//        educations.forEach(x -> {
//                    if (Objects.equals(x.getCandidateCandidateEducationId(), educationId)) {
//                        educations.remove(x);
//                        CandidateCandidateEducation edu = candidateCandidateEducationMapper.dtoToEntity(dto);
//                        edu.setCandidateCandidateEducationId(educationId);
//                        educations.add(edu);
//                    }
//                }
//        );
//        return educations;
//    }

    public void save(Candidate candidate, EducationDTO dto) {
        if (Objects.isNull(dto.getId())) {
            create(candidate, dto);
        } else {
            update(candidate, dto);
        }
    }

    public void create(Candidate candidate, EducationDTO dto) {
        educationRepository.save(checkValid(candidate, dto));
    }

    private CandidateEducation checkValid(Candidate candidate, EducationDTO dto) {
        CandidateEducation c = educationMapper.dtoToEntity(dto);
        c.setCandidate(candidate);
        return c;
    }

    public void update(Candidate candidate, EducationDTO _dto) {
        Optional<CandidateEducation> op = educationRepository.findById(_dto.getId());
        CandidateEducation education = checkValid(candidate, _dto);
        if (op.isPresent()) {
            CandidateEducation r = op.get();
            r.setSchoolName(education.getSchoolName());
            r.setMajor(education.getMajor());
            r.setStartDate(education.getStartDate());
            r.setEndDate(education.getEndDate());
            r.setDescription(education.getDescription());
            r.setDegree(education.getDegree());
            educationRepository.save(r);
        } else {
            throw new ResourceNotFoundException("Không tìm thấy học vấn!");
        }
    }
}
