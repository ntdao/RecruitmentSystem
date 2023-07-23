package com.recruitmentsystem.controller;

import com.recruitmentsystem.common.exception.ResourceAlreadyExistsException;
import com.recruitmentsystem.common.exception.ResourceNotFoundException;
import com.recruitmentsystem.entity.JobLevel;
import com.recruitmentsystem.service.JobLevelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/job_level")
@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_HR')")
@RequiredArgsConstructor
public class JobLevelController {
    private final JobLevelService jobLevelService;

    @GetMapping()
    public ResponseEntity<?> getAllJobLeveles() {
        List<JobLevel> companies = jobLevelService.findAllJobLevels();
        return ResponseEntity.ok(companies);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<?> getJobLevelById(@PathVariable("id") Integer id) {
        JobLevel jobLevel;
        try {
            jobLevel = jobLevelService.findById(id);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        return ResponseEntity.ok(jobLevel);
    }

    @GetMapping("/find/{name}")
    public ResponseEntity<?> getJobLevelByName(@PathVariable("name") String name) {
//        CompanyJobLevel jobLevel;
//        try {
//            jobLevel = jobLevelService.findJobLevelByJobLevelName(name);
//        } catch (ResourceNotFoundException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
//        }
//        return ResponseEntity.ok(jobLevel);
        return ResponseEntity.ok(jobLevelService.findByJobLevelName(name));
    }

    @PostMapping("/add")
    public ResponseEntity<?> registerJobLevel(@RequestBody JobLevel request) {
        try {
            jobLevelService.addJobLevel(request);
        } catch (ResourceAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateJobLevel(@PathVariable("id") Integer id,
                                          @RequestBody JobLevel request) {
        try {
            jobLevelService.updateJobLevel(id, request);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_HR')")
    public ResponseEntity deleteJobLevel(@PathVariable("id") Integer id) {
        try {
            jobLevelService.deleteJobLevel(id);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

}
