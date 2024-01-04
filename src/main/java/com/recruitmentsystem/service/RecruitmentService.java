package com.recruitmentsystem.service;

import com.recruitmentsystem.dto.InterviewDto;
import com.recruitmentsystem.dto.UserResponseModel;
import com.recruitmentsystem.entity.Interview;
import com.recruitmentsystem.entity.Recruitment;
import com.recruitmentsystem.entity.Job;
import com.recruitmentsystem.entity.User;
import com.recruitmentsystem.exception.ResourceNotFoundException;
import com.recruitmentsystem.repository.RecruitmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RecruitmentService {
    private final InterviewService interviewService;
    private final JobService jobService;
    private final NotificationService notificationService;
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

    public List<UserResponseModel> getAllByJob(Integer jobId, List<Integer> status) {
        List<Integer> ids = recruitmentRepository.findAllByStatus(jobId, status);
        return userService.findUsersByIds(ids);
    }

    public void changeStatus(Integer recruitmentId, Integer status) {
        recruitmentRepository.changeStatus(recruitmentId, status);
        String message = "";
        switch (status) {
            case 1:
                message = "Bạn đã qua vòng hồ sơ công việc %s";
                break;
            case 2:
                message = "Bạn đã rớt vòng hồ sơ công việc %s";
                break;
            case 3:
                message = "Bạn đã qua vòng phỏng vấn công việc %s";
                break;
            case 4:
                message = "Bạn đã rớt vòng phỏng vấn công việc %s";
                break;
        }
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
        recruitment.setInterviews((Set<Interview>) interviewService.addInterview(dto));
        recruitmentRepository.save(recruitment);
    }
}
