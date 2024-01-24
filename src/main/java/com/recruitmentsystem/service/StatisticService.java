package com.recruitmentsystem.service;

import com.recruitmentsystem.dto.JobResponseModel;
import com.recruitmentsystem.dto.StatisticDto;
import com.recruitmentsystem.entity.Job;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StatisticService {
    private final CandidateService candidateService;
    private final CompanyService companyService;
    private final JobService jobService;
    private final RecruitmentService recruitmentService;

    public StatisticDto getCandidate() {
        return candidateService.getQuantity();
    }

    public StatisticDto getCompany() {
        return companyService.getQuantity();
    }

    public StatisticDto getJob() {
        return jobService.getQuantity();
    }

    public Map<String, Object> getAll() {
        Map<String, Object> res = new HashMap<>();
        Long candidateQuantity = candidateService.getQuantity().total();
        Long companyQuantity = companyService.getQuantity().total();
        List<JobResponseModel> jobs = jobService.findAllJobsByAdmin();
        int jobQuantity = jobs.size();
        int applicationQuantity = jobs.stream().mapToInt(x -> recruitmentService.getCandidateQuantityByJob(x.jobId())).sum();

        res.put("user", candidateQuantity);
        res.put("company", companyQuantity);
        res.put("job", jobQuantity);
        res.put("application", applicationQuantity);

        return res;
    }

    public Map<String, Object> getByCompany(Integer companyId) {
        List<Job> jobs = jobService.findByCompanyId(companyId);
        int jobQuantity = jobs.size();
        int applicationQuantity = jobs.stream().mapToInt(x -> recruitmentService.getCandidateQuantityByJob(x.getJobId())).sum();

//        int passQuantity = jobs.stream().mapToInt(x -> recruitmentService.getCandidateQuantityByJob(x.getJobId())).sum();
//        int failQuantity = jobs.stream().mapToInt(x -> recruitmentService.getCandidateQuantityByJob(x.getJobId())).sum();

        Map<String, Object> res = new HashMap<>();
        res.put("job", jobQuantity);
        res.put("application", applicationQuantity);

        return res;
    }
}
