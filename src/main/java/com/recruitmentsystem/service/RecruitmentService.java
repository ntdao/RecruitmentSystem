package com.recruitmentsystem.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recruitmentsystem.dto.InterviewDTO;
import com.recruitmentsystem.dto.RecruitmentDto;
import com.recruitmentsystem.entity.Candidate;
import com.recruitmentsystem.entity.Interview;
import com.recruitmentsystem.entity.Job;
import com.recruitmentsystem.entity.Recruitment;
import com.recruitmentsystem.exception.ResourceNotFoundException;
import com.recruitmentsystem.mapper.RecruitmentMapper;
import com.recruitmentsystem.repository.InterviewRepository;
import com.recruitmentsystem.repository.RecruitmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class RecruitmentService {
    private final ObjectMapper objectMapper;
    private final InterviewRepository interviewRepository;
    private final JobService jobService;
    private final NotificationService notificationService;
    private final RecruitmentMapper recruitmentMapper;
    private final RecruitmentRepository recruitmentRepository;
    private final CandidateService userService;
    private final EmailService emailService;

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
            status = Arrays.asList(0, 1, 2, 3, 4, 5);
        } else {
            status = Collections.singletonList(Integer.parseInt(statusId));
        }
        return getAllByJob(Integer.parseInt(jobId), status);
    }

    public List<RecruitmentDto> getAllIntervieweeByJob(String jobId, String statusId) {
        List<Integer> status;
        if (jobId.equals("")) return null;
        if (Objects.equals(statusId, "")) {
            status = Arrays.asList(3, 4, 5);
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

    @Transactional
    public void changeStatus(Integer recruitmentId, Integer status) {
        recruitmentRepository.changeStatus(recruitmentId, status);
        String message = switch (status) {
            case 1 -> "Bạn đã qua vòng hồ sơ công việc %s";
            case 2 -> "Bạn đã trượt vòng hồ sơ công việc %s";
            case 4 -> "Bạn đã qua vòng phỏng vấn công việc %s";
            case 5 -> "Bạn đã trượt vòng phỏng vấn công việc %s";
            default -> "";
        };
        Recruitment recruitment = findById(recruitmentId);
        Candidate candidate = recruitment.getCandidate();
        String jobName = recruitment.getJob().getJobName();
        String content = String.format(message, jobName);
        String body = "Đã có kết quả ứng tuyển công việc " + jobName;
        String link = "http://localhost:3000/login";
        notificationService.addNotification(content, recruitment.getCandidate().getAccount());
        emailService.sendNotification(candidate.getFullName(), candidate.getAccount().getEmail(), body, link);
    }

    public Recruitment findById(Integer recruitmentId) {
        return recruitmentRepository.findById(recruitmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));
    }

    @Transactional
    public void addInterview(Integer recruitmentId, InterviewDTO dto) {
        Recruitment recruitment = findById(recruitmentId);
        recruitment.setApplicationStatus(3);

        Interview interview = objectMapper.convertValue(dto, Interview.class);
        interview.setInterviewStatus(0);
        interview.setRecruitment(recruitment);
        interviewRepository.save(interview);

        String message = "Lịch phỏng vấn công việc %s đã được cập nhật";
        Candidate candidate = recruitment.getCandidate();
        String jobName = recruitment.getJob().getJobName();
        String content = String.format(message, jobName);
        String link = "http://localhost:3000/login";
        notificationService.addNotification(content, recruitment.getCandidate().getAccount());
        emailService.sendNotification(candidate.getFullName(), candidate.getAccount().getEmail(), message, link);
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
        return recruitmentRepository.countByJob(jobId);
    }
}
