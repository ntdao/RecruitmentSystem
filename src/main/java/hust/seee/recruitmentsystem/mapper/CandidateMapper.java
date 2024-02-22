package hust.seee.recruitmentsystem.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import hust.seee.recruitmentsystem.dto.*;
import hust.seee.recruitmentsystem.entity.Candidate;
import hust.seee.recruitmentsystem.enums.Gender;
import hust.seee.recruitmentsystem.service.CategoryService;
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

    public CandidateDTO entityToDto(Candidate candidate) {
        AddressDTO address = addressMapper.entityToDto(candidate.getAddress());
        Set<EducationDTO> educations = educationMapper.entitiesToDtos(candidate.getCandidateEducations());
        List<HistoryDTO> histories = candidate.getWorkingHistories()
                .stream()
                .map(h -> objectMapper.convertValue(h, HistoryDTO.class))
                .toList();
        List<SkillDTO> skills = candidate.getCandidateSkills()
                .stream()
                .map(s -> objectMapper.convertValue(s, SkillDTO.class))
                .toList();
        CategoryDTO category = objectMapper.convertValue(candidate.getCategory(), CategoryDTO.class);

        return CandidateDTO
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

    public Candidate dtoToEntity(CandidateDTO request) {
//        if (request.getPassword() == null
//                && request.getImgUrl() == null
//                && request.getRoleName() == null) {
//            return Candidate
//                    .builder()
//                    .fullName(request.getFullName())
//                    .phoneNumber(request.getPhoneNumber())
//                    .gender(Gender.valueOf(request.getGender()))
//                    .birthday(request.getBirthday())
//                    .desiredJob(request.getDesiredJob())
//                    .educationLevel(request.getEducationLevel())
//                    .category(categoryService.findById(request.getCategoryId()))
//                    .build();
//        } else {
        return Candidate
                .builder()
                .fullName(request.getFullName())
                .phoneNumber(request.getPhoneNumber())
                .gender(Gender.valueOf(request.getGender()))
                .birthday(request.getBirthday())
                .desiredJob(request.getDesiredJob())
                .educationLevel(request.getEducationLevel())
                .category(categoryService.findById(request.getCategoryId()))
                .imgUrl(request.getImgUrl())
                .build();
//        }
    }
}
