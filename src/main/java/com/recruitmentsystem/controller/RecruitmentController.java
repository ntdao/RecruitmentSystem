package com.recruitmentsystem.controller;

import com.recruitmentsystem.dto.InterviewDto;
import com.recruitmentsystem.dto.UserResponseModel;
import com.recruitmentsystem.service.RecruitmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @GetMapping("/job/{jobId}/all-candidate")
    @PreAuthorize("hasAuthority('COMPANY')")
    public List<UserResponseModel> getAllByJob(@PathVariable("jobId") Integer jobId) {
        return recruitmentService.getAllByJob(jobId, Arrays.asList(0, 1, 2));
    }

    @GetMapping("/job/{jobId}/all-consider-cv")
    @PreAuthorize("hasAuthority('COMPANY')")
    public List<UserResponseModel> getAllConsiderCVByJob(@PathVariable("jobId") Integer jobId) {
        return recruitmentService.getAllByJob(jobId, Arrays.asList(0));
    }

    @GetMapping("/job/{jobId}/all-pass-cv")
    @PreAuthorize("hasAuthority('COMPANY')")
    public List<UserResponseModel> getAllPassCVByJob(@PathVariable("jobId") Integer jobId) {
        return recruitmentService.getAllByJob(jobId, Arrays.asList(1));
    }

    @GetMapping("/job/{jobId}/all-fail-cv")
    @PreAuthorize("hasAuthority('COMPANY')")
    public List<UserResponseModel> getAllFailCVByJob(@PathVariable("jobId") Integer jobId) {
        return recruitmentService.getAllByJob(jobId, Arrays.asList(2));
    }

    @GetMapping("/job/{jobId}/all-interview")
    @PreAuthorize("hasAuthority('COMPANY')")
    public List<UserResponseModel> getAllInterviewByJob(@PathVariable("jobId") Integer jobId) {
        return recruitmentService.getAllByJob(jobId, Arrays.asList(3,4,5));
    }

    @GetMapping("/job/{jobId}/all-consider-interview")
    @PreAuthorize("hasAuthority('COMPANY')")
    public List<UserResponseModel> getAllConsiderInterviewByJob(@PathVariable("jobId") Integer jobId) {
        return recruitmentService.getAllByJob(jobId, Arrays.asList(3));
    }

    @GetMapping("/job/{jobId}/all-pass-interview")
    @PreAuthorize("hasAuthority('COMPANY')")
    public List<UserResponseModel> getAllPassInterviewByJob(@PathVariable("jobId") Integer jobId) {
        return recruitmentService.getAllByJob(jobId, Arrays.asList(4));
    }

    @GetMapping("/job/{jobId}/all-fail-interview")
    @PreAuthorize("hasAuthority('COMPANY')")
    public List<UserResponseModel> getAllFailInterviewByJob(@PathVariable("jobId") Integer jobId) {
        return recruitmentService.getAllByJob(jobId, Arrays.asList(5));
    }

    @PostMapping("/change-status/{applicationId}")
    @PreAuthorize("hasAuthority('COMPANY')")
    public void changeStatus(@PathVariable("applicationId") Integer applicationId,
                             @RequestBody Map<String, Integer> request) {
        recruitmentService.changeStatus(applicationId, request.get("status"));
    }

    @PostMapping("/add-interview")
    @PreAuthorize("hasAuthority('COMPANY')")
    public void addInterview(@PathVariable("applicationId") Integer applicationId,
                             @RequestBody InterviewDto dto) {
       recruitmentService.addInterview(applicationId, dto);
    }
}
