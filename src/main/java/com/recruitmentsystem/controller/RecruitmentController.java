package com.recruitmentsystem.controller;

import com.recruitmentsystem.dto.InterviewDto;
import com.recruitmentsystem.dto.RecruitmentDto;
import com.recruitmentsystem.dto.UserResponseModel;
import com.recruitmentsystem.security.config.ApplicationConfiguration;
import com.recruitmentsystem.service.RecruitmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Arrays;
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

    @GetMapping("/job/{jobId}/all-candidate")
    public List<RecruitmentDto> getAllByJob(@PathVariable("jobId") Integer jobId) {
        return recruitmentService.getAllByJob(jobId, Arrays.asList(0, 1, 2));
    }

    @GetMapping("/job/{jobId}/all-consider-cv")
    public List<RecruitmentDto> getAllConsiderCVByJob(@PathVariable("jobId") Integer jobId) {
        return recruitmentService.getAllByJob(jobId, Arrays.asList(0));
    }

    @GetMapping("/job/{jobId}/all-pass-cv")
    public List<RecruitmentDto> getAllPassCVByJob(@PathVariable("jobId") Integer jobId) {
        return recruitmentService.getAllByJob(jobId, Arrays.asList(1));
    }

    @GetMapping("/job/{jobId}/all-fail-cv")
    public List<RecruitmentDto> getAllFailCVByJob(@PathVariable("jobId") Integer jobId) {
        return recruitmentService.getAllByJob(jobId, Arrays.asList(2));
    }

    @GetMapping("/job/{jobId}/all-interview")
    public List<RecruitmentDto> getAllInterviewByJob(@PathVariable("jobId") Integer jobId) {
        return recruitmentService.getAllByJob(jobId, Arrays.asList(3,4,5));
    }

    @GetMapping("/job/{jobId}/all-consider-interview")
    public List<RecruitmentDto> getAllConsiderInterviewByJob(@PathVariable("jobId") Integer jobId) {
        return recruitmentService.getAllByJob(jobId, Arrays.asList(3));
    }

    @GetMapping("/job/{jobId}/all-pass-interview")
    public List<RecruitmentDto> getAllPassInterviewByJob(@PathVariable("jobId") Integer jobId) {
        return recruitmentService.getAllByJob(jobId, Arrays.asList(4));
    }

    @GetMapping("/job/{jobId}/all-fail-interview")
    public List<RecruitmentDto> getAllFailInterviewByJob(@PathVariable("jobId") Integer jobId) {
        return recruitmentService.getAllByJob(jobId, Arrays.asList(5));
    }

    @PostMapping("/change-status/{applicationId}")
    public void changeStatus(@PathVariable("applicationId") Integer applicationId,
                             @RequestBody Map<String, Integer> request) {
        recruitmentService.changeStatus(applicationId, request.get("status"));
    }

    @PostMapping("/add-interview")
    public void addInterview(@PathVariable("applicationId") Integer applicationId,
                             @RequestBody InterviewDto dto) {
       recruitmentService.addInterview(applicationId, dto);
    }
}
