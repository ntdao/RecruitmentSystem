package com.recruitmentsystem.mapper;

import com.recruitmentsystem.dto.RecruitmentDto;
import com.recruitmentsystem.entity.Recruitment;
import com.recruitmentsystem.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecruitmentMapper {
    public RecruitmentDto entityToDto(Recruitment recruitment) {
        return RecruitmentDto.builder()
                .applicationId(recruitment.getApplicationId())
                .candidateFullName(recruitment.getCandidate().getFullName())
                .candidateImgUrl(recruitment.getCandidate().getImgUrl())
                .candidateBirthday(recruitment.getCandidate().getBirthday())
                .candidateAddress(recruitment.getCandidate().getAddress().getFullAddress())
                .jobName(recruitment.getJob().getJobName())
                .companyName(recruitment.getJob().getCompany().getCompanyFullName())
                .companyLogo(recruitment.getJob().getCompany().getCompanyLogo())
                .jobQuantity(recruitment.getJob().getJobQuantity().toString())
                .jobCandidate("")
                .applicationTimeAgo(Utils.calculateTimeAgo(recruitment.getCreateDate()))
                .applicationStatus(recruitment.getApplicationStatus())
                .build();
    }
}
