//package hust.seee.recruitmentsystem.service;
//
//import hust.seee.recruitmentsystem.dto.JobDTO;
//import hust.seee.recruitmentsystem.dto.StatisticDTO;
//import hust.seee.recruitmentsystem.entity.Job;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@Service
//@RequiredArgsConstructor
//public class StatisticService {
//    private final CandidateService candidateService;
//    private final CompanyService companyService;
//    private final JobService jobService;
//    private final RecruitmentService recruitmentService;
//
//    public StatisticDTO getCandidate() {
//        return candidateService.getQuantity();
//    }
//
//    public StatisticDTO getCompany() {
//        return companyService.getQuantity();
//    }
//
//    public StatisticDTO getJob() {
//        return jobService.getQuantity();
//    }
//
//    public Map<String, Object> getAll() {
//        Map<String, Object> res = new HashMap<>();
//        Long candidateQuantity = candidateService.getQuantity().getTotal();
//        Long companyQuantity = companyService.getQuantity().getTotal();
//        List<JobDTO> jobs = jobService.findAllJobsByAdmin();
//        int jobQuantity = jobs.size();
//        int applicationQuantity = jobs.stream().mapToInt(x -> recruitmentService.getCandidateQuantityByJob(x.getJobId())).sum();
//
//        res.put("user", candidateQuantity);
//        res.put("company", companyQuantity);
//        res.put("job", jobQuantity);
//        res.put("application", applicationQuantity);
//
//        return res;
//    }
//
//    public Map<String, Object> getByCompany(Integer companyId) {
//        List<Job> jobs = jobService.findByCompanyId(companyId);
//        int jobQuantity = jobs.size();
//        int applicationQuantity = jobs.stream().mapToInt(x -> recruitmentService.getCandidateQuantityByJob(x.getJobId())).sum();
//
////        int passQuantity = jobs.stream().mapToInt(x -> recruitmentService.getCandidateQuantityByJob(x.getJobId())).sum();
////        int failQuantity = jobs.stream().mapToInt(x -> recruitmentService.getCandidateQuantityByJob(x.getJobId())).sum();
//
//        Map<String, Object> res = new HashMap<>();
//        res.put("job", jobQuantity);
//        res.put("application", applicationQuantity);
//
//        return res;
//    }
//}
