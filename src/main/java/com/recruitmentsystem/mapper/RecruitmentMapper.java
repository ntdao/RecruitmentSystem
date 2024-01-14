package com.recruitmentsystem.mapper;

import com.recruitmentsystem.dto.RecruitmentDto;
import com.recruitmentsystem.entity.Recruitment;
import com.recruitmentsystem.repository.RecruitmentRepository;
import com.recruitmentsystem.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class RecruitmentMapper {
    private final RecruitmentRepository recruitmentRepository;
    public RecruitmentDto entityToDto(Recruitment recruitment) {
        Integer jobId = recruitment.getJob().getJobId();
        Integer jobCandidateQuantity =  recruitmentRepository.findAllByStatus(jobId, Arrays.asList(0, 1, 2)).size();
        return RecruitmentDto.builder()
                .applicationId(recruitment.getApplicationId())
                .candidateId(recruitment.getCandidate().getCandidateId())
                .candidateFullName(recruitment.getCandidate().getFullName())
                .candidateImgUrl(recruitment.getCandidate().getImgUrl())
                .candidateBirthday(recruitment.getCandidate().getBirthday())
                .candidateAddress(recruitment.getCandidate().getAddress().getFullAddress())
                .jobId(jobId)
                .jobName(recruitment.getJob().getJobName())
                .companyName(recruitment.getJob().getCompany().getCompanyFullName())
                .companyLogo(recruitment.getJob().getCompany().getCompanyLogo())
                .jobQuantity(recruitment.getJob().getJobQuantity().toString())
                .jobCandidate(jobCandidateQuantity.toString())
                .applicationTimeAgo(Utils.calculateTimeAgo(recruitment.getCreateDate()))
                .applicationStatus(recruitment.getApplicationStatus())
                .build();
    }
}
