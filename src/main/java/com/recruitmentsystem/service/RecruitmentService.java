package com.recruitmentsystem.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recruitmentsystem.dto.InterviewDto;
import com.recruitmentsystem.dto.RecruitmentDto;
import com.recruitmentsystem.dto.UserResponseModel;
import com.recruitmentsystem.entity.Interview;
import com.recruitmentsystem.entity.Job;
import com.recruitmentsystem.entity.Recruitment;
import com.recruitmentsystem.entity.User;
import com.recruitmentsystem.exception.ResourceNotFoundException;
import com.recruitmentsystem.mapper.RecruitmentMapper;
import com.recruitmentsystem.repository.RecruitmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RecruitmentService {
    private final InterviewService interviewService;
    private final JobService jobService;
    private final NotificationService notificationService;
    private final RecruitmentMapper recruitmentMapper;
    private final RecruitmentRepository recruitmentRepository;
    private final UserService userService;

    public void candidateApplyJob(Principal principal, Integer jobId) {
        User user = userService.getCurrentUser(principal);
        Job job = jobService.findById(jobId);
        Recruitment recruitment = Recruitment.builder()
                .user(user)
                .job(job)
                .applicationStatus(0)
                .createDate(LocalDateTime.now())
                .build();
        recruitmentRepository.save(recruitment);
        String content = String.format("%s ứng tuyển công việc %s", user.getFullName(), job.getJobName());
        notificationService.addNotification(content, job.getCompany().getAccount());
    }

    public List<RecruitmentDto> getAllByJob(Integer jobId, List<Integer> status) {
        List<Integer> ids = recruitmentRepository.findAllByStatus(jobId, status);
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
        notificationService.addNotification(content, findById(recruitmentId).getUser().getAccount());
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
        User user = userService.getCurrentUser(principal);
        List<Recruitment> recruitments = recruitmentRepository.findAllByCandidateId(user.getUserId());
        return recruitments.stream()
                .map(recruitmentMapper::entityToDto)
                .toList();
    }

    public boolean checkApplyJob(Principal principal, Integer jobId) {
        User user = userService.getCurrentUser(principal);
        return recruitmentRepository.isApplied(user.getUserId(), jobId);
    }

    public Recruitment getByApplicationId(Integer applicationId) {
        return recruitmentRepository.findByApplicationId(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application with id " + applicationId + " not found"));
    }

    public RecruitmentDto getDtoByApplicationId(Integer applicationId) {
        return recruitmentMapper.entityToDto(getByApplicationId(applicationId));
    }
}
