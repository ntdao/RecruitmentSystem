package com.recruitmentsystem.service;

import com.recruitmentsystem.entity.Candidate;
import com.recruitmentsystem.entity.Job;
import com.recruitmentsystem.entity.Province;
import com.recruitmentsystem.entity.Skill;
import com.recruitmentsystem.enums.Gender;
import com.recruitmentsystem.repository.CandidateRepository;
import com.recruitmentsystem.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class MatchingService {
    public final double MAX_POINT_CATEGORY = 3;
    public final double MAX_POINT_SKILL = 4;
    public final double MAX_POINT_EDU = 1;
    public final double MAX_POINT_GENDER = 1;
    public final double MAX_POINT_ADDRESS = 1;
    private final ProvinceService provinceService;
    private final CandidateRepository candidateRepository;
    private final JobRepository jobRepository;
    private final EmailService emailService;

    public double calculateCompatibilitySkills(Job job, Candidate candidate) {
        double pointPerSkill = MAX_POINT_SKILL / job.getJobSkills().size();
        double compatibilityScore = 0.0;

        Set<Skill> jobSkills = job.getJobSkills();
        Set<Skill> candidateSkills = candidate.getCandidateSkills();

        for (Skill skill : jobSkills) {
            for (Skill candidateSkill : candidateSkills) {
                if (Objects.equals(skill.getId(), candidateSkill.getId())) {
                    compatibilityScore += pointPerSkill;
                }
            }
        }
        return compatibilityScore;
    }

//    public double calculateCompatibilityTimeExp(Job job, Candidate candidate) {
//        if(candidate.timeExp >= job.timeExp) return MAX_POINT_TIME_EXP;
//        return MAX_POINT_TIME_EXP * (candidate.timeExp / job.timeExp);
//    }

    public double calculateCompatibilityGender(Job job, Candidate candidate) {
        if (job.getJobGender() == Gender.OTHER || candidate.getGender() == job.getJobGender())
            return MAX_POINT_GENDER;
        return 0.0;
    }

    public double calculateCompatibilityCategory(Job job, Candidate candidate) {
        if (Objects.equals(candidate.getCategory().getId(), job.getCategory().getId())) return MAX_POINT_CATEGORY;
        return 0.0;
    }

    public double calculateCompatibilityEducation(Job job, Candidate candidate) {
        if (Objects.equals(candidate.getEducationLevel(), job.getMinEducationLevel())) return MAX_POINT_EDU;
        return 0.0;
    }

    public double calculateCompatibilityAddress(Job job, Candidate candidate) {
        Province candidateProvince = provinceService.findProvinceByWardCode(candidate.getAddress().getWard().getWardCode());
        Province jobProvince = provinceService.findProvinceByWardCode(job.getJobAddress().getWard().getWardCode());

        if (Objects.equals(candidateProvince.getProvinceCode(), jobProvince.getProvinceCode()))
            return MAX_POINT_ADDRESS;
        return 0.0;
    }

    private double calculateCompatibility(Job job, Candidate candidate) {
        double compatibility = calculateCompatibilitySkills(job, candidate)
                + calculateCompatibilityCategory(job, candidate)
                + calculateCompatibilityGender(job, candidate)
                + calculateCompatibilityEducation(job, candidate);

        if (candidate.getAddress() != null) {
            compatibility += calculateCompatibilityAddress(job, candidate);
        }

        return compatibility;
    }


    public Map<Candidate, List<Job>> findTopRecommendedJobsForEachCandidate(List<Candidate> candidates, List<Job> jobs, List<Job> jobTop) {
        Map<Candidate, List<Job>> topRecommendedJobsForEachCandidate = new HashMap<>();

        for (Candidate candidate : candidates) {
            PriorityQueue<Job> topJobs = new PriorityQueue<>(Comparator.comparingDouble(job -> calculateCompatibility(job, candidate)));
            List<Job> candidateJobs = (candidate.getAddress() == null ? jobTop : jobs);
            for (Job job : candidateJobs) {
//                double compatibility = calculateCompatibility(job, candidate);
                topJobs.offer(job);
                if (topJobs.size() > 5) {
                    topJobs.poll();
                }
            }
            List<Job> recommendedJobs = new ArrayList<>(topJobs);
            Collections.sort(recommendedJobs, Comparator.comparingDouble(job -> calculateCompatibility(job, candidate)));
            topRecommendedJobsForEachCandidate.put(candidate, recommendedJobs);
            emailService.sendRecommendJob(candidate.getFullName(), candidate.getAccount().getEmail(), recommendedJobs);
        }

        return topRecommendedJobsForEachCandidate;
    }

    @Scheduled(cron = "0 0 0 * * *")
//    @PostConstruct
    public void recommendJob() {
        System.out.println("=================================");
        System.out.println("Start recommend job");
        List<Candidate> candidates = candidateRepository.findAllCandidate();
        System.out.println("Candidate: " + candidates);
        List<Job> jobs = jobRepository.findAllJob().stream().filter(j -> j.getJobStatus() == 0).toList();
        System.out.println("Job: " + jobs);
        // nếu ứng viên không có địa chỉ thì gợi ý công vệc ở HN/HCM
        List<Job> jobTop = jobs.stream().filter(job -> {
                    Province p = provinceService.findProvinceByWardCode(job.getJobAddress().getWard().getWardCode());
                    return p.getProvinceCode().equals("01") || p.getProvinceCode().equals("79");
                }
        ).toList();
        System.out.println("top job: " + jobTop);

        Map<Candidate, List<Job>> map = findTopRecommendedJobsForEachCandidate(candidates, jobs, jobTop);
        System.out.println("Stop recommend job");
        System.out.println("=================================");
    }
}
