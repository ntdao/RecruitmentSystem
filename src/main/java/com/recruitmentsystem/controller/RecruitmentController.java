package com.recruitmentsystem.controller;

import com.recruitmentsystem.dto.InterviewDto;
import com.recruitmentsystem.dto.RecruitmentDto;
import com.recruitmentsystem.service.RecruitmentService;
import lombok.RequiredArgsConstructor;
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

    @GetMapping("/manage/job/{jobId}/all-candidate")
    public List<RecruitmentDto> getAllByJob(@PathVariable("jobId") Integer jobId) {
        return recruitmentService.getAllByJob(jobId, Arrays.asList(0, 1, 2));
    }

    @GetMapping("/manage/job/{jobId}/candidate-quantity")
    public Integer getCandidateQuantityByJob(@PathVariable("jobId") Integer jobId) {
        return recruitmentService.getCandidateQuantityByJob(jobId);
    }

    @GetMapping("/manage/job/{jobId}/all-consider-cv")
    public List<RecruitmentDto> getAllConsiderCVByJob(@PathVariable("jobId") Integer jobId) {
        return recruitmentService.getAllByJob(jobId, List.of(0));
    }

    @GetMapping("/manage/job/{jobId}/all-pass-cv")
    public List<RecruitmentDto> getAllPassCVByJob(@PathVariable("jobId") Integer jobId) {
        return recruitmentService.getAllByJob(jobId, List.of(1));
    }

    @GetMapping("/manage/job/{jobId}/all-fail-cv")
    public List<RecruitmentDto> getAllFailCVByJob(@PathVariable("jobId") Integer jobId) {
        return recruitmentService.getAllByJob(jobId, List.of(2));
    }

    @GetMapping("/manage/job/{jobId}/all-interview")
    public List<RecruitmentDto> getAllInterviewByJob(@PathVariable("jobId") Integer jobId) {
        return recruitmentService.getAllByJob(jobId, Arrays.asList(3, 4, 5));
    }

    @GetMapping("/manage/job/{jobId}/all-consider-interview")
    public List<RecruitmentDto> getAllConsiderInterviewByJob(@PathVariable("jobId") Integer jobId) {
        return recruitmentService.getAllByJob(jobId, List.of(3));
    }

    @GetMapping("/manage/job/{jobId}/all-pass-interview")
    public List<RecruitmentDto> getAllPassInterviewByJob(@PathVariable("jobId") Integer jobId) {
        return recruitmentService.getAllByJob(jobId, List.of(4));
    }

    @GetMapping("/manage/job/{jobId}/all-fail-interview")
    public List<RecruitmentDto> getAllFailInterviewByJob(@PathVariable("jobId") Integer jobId) {
        return recruitmentService.getAllByJob(jobId, List.of(5));
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
