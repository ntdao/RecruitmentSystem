package com.recruitmentsystem.controller;

import com.recruitmentsystem.dto.StatisticDto;
import com.recruitmentsystem.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/statistic")
@RequiredArgsConstructor
public class StatisticController {
    private final StatisticService service;

    @GetMapping("/candidate")
    public StatisticDto candidateQuantity() {
        return service.getCandidate();
    }

    @GetMapping("/company")
    public StatisticDto companyQuantity() {
        return service.getCompany();
    }

    @GetMapping("/job")
    public StatisticDto jobQuantity() {
        return service.getJob();
    }

    @GetMapping("/system")
    public Map<String, Object> jobQuantityByCompany() {
        return service.getAll();
    }

    @GetMapping("/{companyId}")
    public Map<String, Object> jobQuantityByCompany(@PathVariable("companyId") Integer companyId) {
        return service.getByCompany(companyId);
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
