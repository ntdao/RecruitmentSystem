package com.recruitmentsystem.usereducation;

import com.recruitmentsystem.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserEducationService {
    private final UserEducationMapper userEducationMapper;
    private final UserEducationRepository userEducationRepository;
    public List<UserEducation> addUserEducation(List<UserEducation> list, UserEducationDto userEducationDto) {
        UserEducation userEducation = userEducationMapper.dtoToEntity(userEducationDto);
        list.add(userEducation);
        return list;
    }
    public List<UserEducation> findAll() {
        return userEducationRepository.findAll();
    }

    public List<UserEducationDto> findByUser(Integer userId) {
        return userEducationRepository.findUserEducationByUser(userId)
                .stream()
                .map(userEducationMapper::entityToDto)
                .collect(Collectors.toList());
    }
}
