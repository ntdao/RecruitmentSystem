package com.recruitmentsystem.mapper;

import com.recruitmentsystem.dto.RecruitmentDto;
import com.recruitmentsystem.entity.Recruitment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RecruitmentMapper {
    private final UserMapper userMapper;
    private final JobMapper jobMapper;

    public RecruitmentDto entityToDto(Recruitment recruitment) {
        return RecruitmentDto.builder()
                .applicationId(recruitment.getApplicationId())
                .candidateFullName(recruitment.getUser().getFullName())
                .candidateImgUrl(recruitment.getUser().getImgUrl())
                .candidateBirthday(recruitment.getUser().getBirthday())
                .candidateAddress(recruitment.getUser().getAddress().getFullAddress())
                .jobName(recruitment.getJob().getJobName())
                .companyName(recruitment.getJob().getCompany().getCompanyFullName())
                .companyLogo(recruitment.getJob().getCompany().getCompanyLogo())
                .jobQuantity(recruitment.getJob().getJobQuantity().toString())
                .jobCandidate("")
                .jobInterviewee("")
                .applicationTime(recruitment.getCreateDate())
                .applicationStatus(recruitment.getApplicationStatus())
                .build();
    }
}
