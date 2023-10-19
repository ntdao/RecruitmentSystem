package com.recruitmentsystem.controller;

import com.recruitmentsystem.common.exception.ResourceAlreadyExistsException;
import com.recruitmentsystem.common.exception.ResourceNotFoundException;
import com.recruitmentsystem.entity.Job;
import com.recruitmentsystem.entity.Job;
import com.recruitmentsystem.model.job.JobDisplayModel;
import com.recruitmentsystem.model.job.JobRequestModel;
import com.recruitmentsystem.service.JobService;
import com.recruitmentsystem.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class JobController {
    private final JobService jobService;

    @GetMapping("/jobs/all")
    public ResponseEntity<List<JobDisplayModel>> getAllJobs() {
        List<JobDisplayModel> jobs = jobService.findAllJobs();
        return ResponseEntity.ok(jobs);
    }

    @GetMapping("/jobs/top")
    public ResponseEntity<?> getTopJobs(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "6") Integer pageSize,
            @RequestParam(defaultValue = "salary") String sortBy) {
        List<JobDisplayModel> list = jobService.getTopJobs(pageNo, pageSize, sortBy);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/jobs/find/{name}")
    public ResponseEntity<?> getJobByName(@PathVariable("name") String name) {
        List<JobDisplayModel> jobs = jobService.findJobByJobName(name);
        return ResponseEntity.ok(jobs);
    }

    @GetMapping("/admin/manage/jobs/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<Job>> getAllJobsAdmin() {
        List<Job> jobs = jobService.findAllJobsAdmin();
        return ResponseEntity.ok(jobs);
    }

    @GetMapping("/admin/manage/jobs/find/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getJobById(@PathVariable("id") Integer id) {
        Job job;
        try {
            job = jobService.findJobByIdAdmin(id);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        return ResponseEntity.ok(job);
    }

    @DeleteMapping("/admin/manage/jobs/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity deleteJob(@PathVariable("id") Integer id) {
        try {
            jobService.deleteJob(id);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/hr/manage/jobs/all")
    @PreAuthorize("hasRole('ROLE_HR')")
    public ResponseEntity<List<Job>> getAllJobsHR(String token) {
        List<Job> jobs = jobService.findAllJobsByHR(token);
        return ResponseEntity.ok(jobs);
    }

    @GetMapping("/hr/manage/jobs/find/{id}")
    @PreAuthorize("hasRole('ROLE_HR')")
    public ResponseEntity<?> getJobByIdHR(@PathVariable("id") Integer id) {
        Job job;
        try {
            job = jobService.findJobByIdAdmin(id);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        return ResponseEntity.ok(job);
    }

    @DeleteMapping("/hr/manage/jobs/delete/{id}")
    @PreAuthorize("hasRole('ROLE_HR')")
    public ResponseEntity deleteJobHR(@PathVariable("id") Integer id) {
        try {
            jobService.deleteJob(id);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }


//    @PostMapping("/manage-jobs/add")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    public ResponseEntity<?> addJob(@RequestBody JobRequestModel request) {
//        try {
//            jobService.addJobAdmin(request);
//        } catch (ResourceAlreadyExistsException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
//        }
//        return ResponseEntity.ok().build();
//    }
//
//    @PutMapping("/manage-jobs/update/{id}")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    public ResponseEntity<?> updateJob(@PathVariable("id") Integer id,
//                                           @RequestBody JobRequestModel request) {
//        try {
//            jobService.updateJobByAdmin(id, request);
//        } catch (ResourceNotFoundException e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//        }
//        return ResponseEntity.ok().build();
//    }


}
