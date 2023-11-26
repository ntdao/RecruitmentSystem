package com.recruitmentsystem.job;

import com.recruitmentsystem.common.myEnum.Gender;
import com.recruitmentsystem.jobposition.JobPositionResponseModel;
import com.recruitmentsystem.jobposition.JobPositionService;
import com.recruitmentsystem.jobtype.JobTypeResponseModel;
import com.recruitmentsystem.jobtype.JobTypeService;
import com.recruitmentsystem.pagination.PageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class JobController {
    private final JobService jobService;
    private final JobPositionService jobPositionService;
    private final JobTypeService jobTypeService;

    @GetMapping("/jobs/all")
    public List<JobResponseModel> getAllJobs() {
        return jobService.findAllJob();
    }

    @GetMapping("/jobs/top")
    public List<JobTopModel> getTopJobs(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "6") Integer pageSize,
            @RequestParam(defaultValue = "salaryMax") String sortBy) {
        return jobService.getTopJobs(pageNo, pageSize, sortBy);
    }

    @PostMapping("/jobs/find")
    public List<JobResponseModel> getTopJobs(@RequestBody PageDto pageDto) {
        return jobService.getJobByPaging(pageDto);
    }

    @GetMapping("/jobs/find/{name}")
    public List<JobResponseModel> getJobByName(@PathVariable("name") String name) {
        return jobService.findJobByJobName(name);
    }

    @GetMapping("/jobs/job-type")
    public List<JobTypeResponseModel> getAllJobType() {
        return jobTypeService.findAllJobTypeResponseModel();
    }

    @GetMapping("/jobs/job-position")
    public List<JobPositionResponseModel> getAllJobPosition() {
        return jobPositionService.findAllJobPositionResponseModel();
    }

    @GetMapping("/jobs/job-gender")
    public ResponseEntity<?> getAllJobGender() {
        return ResponseEntity.ok(Gender.getAll());
    }

    @GetMapping("/admin/manage/jobs/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<JobResponseModel> adminGetAllJob() {
        return jobService.findAllJobsByAdmin();
    }

    @GetMapping("/admin/manage/jobs/find/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public JobResponseModel adminGetJobById(@PathVariable("id") Integer id) {
        return jobService.findJobById(id);
    }

    @DeleteMapping("/admin/manage/jobs/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void adminDeleteJob(@PathVariable("id") Integer id) {
        jobService.deleteJob(id);
    }

    @GetMapping("/company/manage/jobs/all")
    @PreAuthorize("hasAuthority('COMPANY')")
    public List<JobResponseModel> getCompanyJob(Principal connectedUser) {
        return jobService.findJobByCompany(connectedUser);
    }

    @GetMapping("/company/manage/jobs/find/{id}")
    @PreAuthorize("hasAuthority('COMPANY')")
    public JobResponseModel companyGetJobById(@PathVariable("id") Integer id) {
        return jobService.findJobById(id);
    }

    @DeleteMapping("/company/manage/jobs/delete/{id}")
    @PreAuthorize("hasAuthority('COMPANY')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void companyDeleteJob(@PathVariable("id") Integer id) {
        jobService.deleteJob(id);
    }
}
