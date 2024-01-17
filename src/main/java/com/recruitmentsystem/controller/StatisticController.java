package com.recruitmentsystem.controller;

import com.recruitmentsystem.dto.StatisticDetailDto;
import com.recruitmentsystem.dto.StatisticDto;
import com.recruitmentsystem.service.CandidateService;
import com.recruitmentsystem.service.CompanyService;
import com.recruitmentsystem.service.JobService;
import com.recruitmentsystem.service.RecruitmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/statistic")
@RequiredArgsConstructor
public class StatisticController {
    private final CandidateService candidateService;
    private final CompanyService companyService;
    private final JobService jobService;
    private final RecruitmentService recruitmentService;

    @GetMapping("/candidate")
    public StatisticDto candidateQuantity() {
        return candidateService.getQuantity();
    }

    @GetMapping("/company")
    public StatisticDto companyQuantity() {
        return companyService.getQuantity();
    }

    @GetMapping("/job")
    public StatisticDto jobQuantity() {
        return jobService.getQuantity();
    }

    @GetMapping("/recruitment")
    public StatisticDto recruitmentQuantity() {
        return null;
    }

    @GetMapping("/pass")
    public Integer passQuantity() {
        return null;
    }

    @GetMapping("/fail")
    public Integer failQuantity() {
        return null;
    }
}
