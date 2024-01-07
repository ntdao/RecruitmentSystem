package com.recruitmentsystem.controller;

import com.recruitmentsystem.enums.Gender;
import com.recruitmentsystem.dto.JobTypeDto;
import com.recruitmentsystem.dto.JobRequestModel;
import com.recruitmentsystem.dto.JobResponseModel;
import com.recruitmentsystem.service.JobService;
import com.recruitmentsystem.dto.JobTopModel;
import com.recruitmentsystem.service.JobTypeService;
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

    @GetMapping("/jobs/find")
    public List<JobResponseModel> getJobByName(@RequestParam("name") String name) {
        return jobService.findJobByJobName(name);
    }

    @GetMapping("/job/{jobId}")
    public JobResponseModel getJobById(@PathVariable("jobId") Integer jobId) {
        return jobService.findJobById(jobId);
    }

    @GetMapping("/jobs/job-type")
    public List<JobTypeDto> getAllJobType() {
        return jobTypeService.findAllJobTypeResponseModel();
    }

    @GetMapping("/jobs/job-gender")
    public ResponseEntity<?> getAllJobGender() {
        return ResponseEntity.ok(Gender.getAll());
    }

    @GetMapping("/jobs/{companyId}/all")
    public List<JobResponseModel> getAllJob(@PathVariable("companyId") Integer id) {
        return jobService.findJobByCompanyId(id);
    }
    @GetMapping("/admin/manage/jobs/all")
    public List<JobResponseModel> adminGetAllJob() {
        return jobService.findAllJobsByAdmin();
    }

    @GetMapping("/manage/jobs/find/{jobId}")
    public JobResponseModel adminGetJobById(@PathVariable("jobId") Integer id) {
        return jobService.findJobById(id);
    }

    @DeleteMapping("/manage/jobs/delete/{jobId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void adminDeleteJob(@PathVariable("jobId") Integer id) {
        jobService.deleteJob(id);
    }

    @GetMapping("/company/manage/jobs/all")
    public List<JobResponseModel> getCompanyJob(Principal connectedUser) {
        return jobService.findJobByCompany(connectedUser);
    }

    @PostMapping("/company/manage/jobs/add")
    public void addJob(@RequestBody JobRequestModel jobRequestModel,
                       Principal principal) {
        jobService.addJob(jobRequestModel, principal);
    }

    @PostMapping("/company/manage/jobs/update/{jobId}")
    public void updateJob(@PathVariable("jobId") Integer jobId,
                          @RequestBody JobRequestModel jobRequestModel) {
        jobService.updateJob(jobId, jobRequestModel);
    }
}
