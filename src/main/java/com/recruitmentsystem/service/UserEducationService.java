package com.recruitmentsystem.service;

import com.recruitmentsystem.entity.UserEducation;
import com.recruitmentsystem.dto.UserEducationDto;
import com.recruitmentsystem.mapper.UserEducationMapper;
import com.recruitmentsystem.repository.UserEducationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserEducationService {
    private final UserEducationMapper userEducationMapper;
    private final UserEducationRepository userEducationRepository;

    public Set<UserEducation> addUserEducation(Set<UserEducation> list, UserEducationDto userEducationDto) {
        UserEducation userEducation = userEducationMapper.dtoToEntity(userEducationDto);
        list.add(userEducation);
        return list;
    }

    public Set<UserEducationDto> findByUser(Integer userId) {
        return userEducationRepository.findUserEducationByUser(userId)
                .stream()
                .map(userEducationMapper::entityToDto)
                .collect(Collectors.toSet());
    }

    public Set<UserEducation> deleteUserEducation(Integer educationId, Set<UserEducation> educations) {
        educations.forEach(e -> {
            if (e.getUserEducationId() == educationId) {
                educations.remove(e);
                userEducationRepository.deleteById(educationId);
            }
        });
        return educations;
    }

    public Set<UserEducation> updateUserEducation(Integer educationId, UserEducationDto dto, Set<UserEducation> educations) {
//        for (int i = 0; i < educations.size(); i++) {
//            if (educations.(i).getUserEducationId() == educationId) {
//                educations.remove(i);
//                UserEducation edu = userEducationMapper.dtoToEntity(dto);
//                edu.setUserEducationId(educationId);
//                educations.add(i, edu);
//            }
//        }
        return educations;
    }
}
