package com.recruitmentsystem.controller;

import com.recruitmentsystem.dto.InterviewDto;
import com.recruitmentsystem.dto.RecruitmentDto;
import com.recruitmentsystem.service.RecruitmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/recruitment")
@RequiredArgsConstructor
public class RecruitmentController {
    private final RecruitmentService recruitmentService;

    @PostMapping("/apply-job/{jobId}")
    public void candidateApplyJob(@PathVariable("jobId") Integer jobId, Principal principal) {
        recruitmentService.candidateApplyJob(principal, jobId);
    }

    @PostMapping("/check-apply/{jobId}")
    public boolean checkApplyJob(@PathVariable("jobId") Integer jobId, Principal principal) {
        return recruitmentService.checkApplyJob(principal, jobId);
    }

    @GetMapping("/candidate/all")
    public List<RecruitmentDto> getAllApplicationByCandidateId(Principal principal) {
        return recruitmentService.getAllByCandidateId(principal);
    }

    @GetMapping("/{applicationId}")
    public RecruitmentDto getByApplicationId(@PathVariable("applicationId") Integer applicationId) {
        return recruitmentService.getDtoByApplicationId(applicationId);
    }

    @GetMapping("/manage/candidate")
    public List<RecruitmentDto> getAllCandidateByJob(@RequestParam String jobId,
                                            @RequestParam String statusId) {
        return recruitmentService.getAllCandidateByJob(jobId, statusId);
    }

    @GetMapping("/manage/interviewee")
    public List<RecruitmentDto> getAllIntervieweeByJob(@RequestParam String jobId,
                                            @RequestParam String statusId) {
        return recruitmentService.getAllIntervieweeByJob(jobId, statusId);
    }

    @GetMapping("/manage/candidate-quantity")
    public Integer getCandidateQuantityByJob(@RequestParam Integer jobId) {
        return recruitmentService.getCandidateQuantityByJob(jobId);
    }

    @PostMapping("/manage/change-status/{applicationId}")
    public void changeStatus(@PathVariable("applicationId") Integer applicationId,
                             @RequestBody Map<String, Integer> request) {
        recruitmentService.changeStatus(applicationId, request.get("status"));
    }

    @PostMapping("/manage/add-interview/{applicationId}")
    public void addInterview(@PathVariable("applicationId") Integer applicationId,
                             @RequestBody InterviewDto dto) {
        recruitmentService.addInterview(applicationId, dto);
    }
}
