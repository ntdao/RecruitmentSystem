package hust.seee.recruitmentsystem.mapper;

import hust.seee.recruitmentsystem.dto.RecruitmentDTO;
import hust.seee.recruitmentsystem.entity.Recruitment;
import hust.seee.recruitmentsystem.repository.RecruitmentRepository;
import hust.seee.recruitmentsystem.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class RecruitmentMapper {
    private final RecruitmentRepository recruitmentRepository;

    public RecruitmentDTO entityToDto(Recruitment recruitment) {
        Integer jobId = recruitment.getJob().getJobId();
        Integer jobCandidateQuantity = recruitmentRepository.findAllByStatus(jobId, Arrays.asList(0, 1, 2)).size();
        return RecruitmentDTO.builder()
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
                .createDate(recruitment.getCreateDate())
                .build();
    }
}
