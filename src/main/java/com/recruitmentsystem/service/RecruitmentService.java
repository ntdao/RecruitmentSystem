package com.recruitmentsystem.service;

import com.recruitmentsystem.dto.InterviewDto;
import com.recruitmentsystem.dto.RecruitmentDto;
import com.recruitmentsystem.entity.Candidate;
import com.recruitmentsystem.entity.Interview;
import com.recruitmentsystem.entity.Job;
import com.recruitmentsystem.entity.Recruitment;
import com.recruitmentsystem.exception.ResourceNotFoundException;
import com.recruitmentsystem.mapper.RecruitmentMapper;
import com.recruitmentsystem.repository.RecruitmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class RecruitmentService {
    private final InterviewService interviewService;
    private final JobService jobService;
    private final NotificationService notificationService;
    private final RecruitmentMapper recruitmentMapper;
    private final RecruitmentRepository recruitmentRepository;
    private final CandidateService userService;

    public void candidateApplyJob(Principal principal, Integer jobId) {
        Candidate candidate = userService.getCurrentCandidate(principal);
        Job job = jobService.findById(jobId);
        Recruitment recruitment = Recruitment.builder()
                .candidate(candidate)
                .job(job)
                .applicationStatus(0)
                .createDate(LocalDateTime.now())
                .build();
        recruitmentRepository.save(recruitment);
        String content = String.format("%s ứng tuyển công việc %s", candidate.getFullName(), job.getJobName());
        notificationService.addNotification(content, job.getCompany().getAccount());
    }

    public List<RecruitmentDto> getAllCandidateByJob(String jobId, String statusId) {
        List<Integer> status;
        if (jobId.equals("")) return null;
        if (Objects.equals(statusId, "")) {
            status = Arrays.asList(0,1,2);
        } else {
            status = Collections.singletonList(Integer.parseInt(statusId));
        }
        return getAllByJob(Integer.parseInt(jobId), status);
    }

    public List<RecruitmentDto> getAllIntervieweeByJob(String jobId, String statusId) {
        List<Integer> status;
        if (jobId.equals("")) return null;
        if (Objects.equals(statusId, "")) {
            status = Arrays.asList(3,4,5);
        } else {
            status = Collections.singletonList(Integer.parseInt(statusId));
        }
        return getAllByJob(Integer.parseInt(jobId), status);
    }

    private List<RecruitmentDto> getAllByJob(Integer jobId, List<Integer> statusId) {
        List<Integer> ids = recruitmentRepository.findAllByStatus(jobId, statusId);
        return recruitmentRepository.findAllByIds(ids)
                .stream()
                .map(recruitmentMapper::entityToDto)
                .toList();
    }

    public void changeStatus(Integer recruitmentId, Integer status) {
        recruitmentRepository.changeStatus(recruitmentId, status);
        String message = switch (status) {
            case 1 -> "Kết quả vòng hồ sơ công việc %s";
            case 2 -> "Kết quả vòng hồ sơ công việc %s";
            case 4 -> "Kết quả vòng phỏng vấn công việc %s";
            case 5 -> "Kết quả vòng phỏng vấn công việc %s";
            default -> "";
        };
        String jobName = findById(recruitmentId).getJob().getJobName();
        String content = String.format(message, jobName);
        System.out.println(content);
        notificationService.addNotification(content, findById(recruitmentId).getCandidate().getAccount());
    }

    public Recruitment findById(Integer recruitmentId) {
        return recruitmentRepository.findById(recruitmentId)
                .orElseThrow(() -> new ResourceNotFoundException(""));
    }

    public void addInterview(Integer recruitmentId, InterviewDto dto) {
        Recruitment recruitment = findById(recruitmentId);
        Set<Interview> interviews = recruitment.getInterviews();
        interviews.add(interviewService.addInterview(dto));
        recruitment.setInterviews(interviews);
        recruitmentRepository.save(recruitment);
    }

    public List<RecruitmentDto> getAllByCandidateId(Principal principal) {
        Candidate candidate = userService.getCurrentCandidate(principal);
        List<Recruitment> recruitments = recruitmentRepository.findAllByCandidateId(candidate.getCandidateId());
        return recruitments.stream()
                .map(recruitmentMapper::entityToDto)
                .toList();
    }

    public boolean checkApplyJob(Principal principal, Integer jobId) {
        Candidate candidate = userService.getCurrentCandidate(principal);
        return recruitmentRepository.isApplied(candidate.getCandidateId(), jobId);
    }

    public Recruitment getByApplicationId(Integer applicationId) {
        return recruitmentRepository.findByApplicationId(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application with id " + applicationId + " not found"));
    }

    public RecruitmentDto getDtoByApplicationId(Integer applicationId) {
        return recruitmentMapper.entityToDto(getByApplicationId(applicationId));
    }

    public Integer getCandidateQuantityByJob(Integer jobId) {
        return recruitmentRepository.findAllByStatus(jobId, Arrays.asList(0, 1, 2)).size();
    }
}