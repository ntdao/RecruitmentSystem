//package com.recruitmentsystem.controller;
//
//import com.recruitmentsystem.common.exception.ResourceAlreadyExistsException;
//import com.recruitmentsystem.common.exception.ResourceNotFoundException;
//import com.recruitmentsystem.entity.JobLevel;
//import com.recruitmentsystem.model.jobLevel.JobLevelDisplayModel;
//import com.recruitmentsystem.model.jobLevel.JobLevelRequestModel;
//import com.recruitmentsystem.service.JobLevelService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("api/v1/job_levels")
//@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_HR')")
//@RequiredArgsConstructor
//public class JobLevelController {
//    private final JobLevelService jobLevelService;
//
//    @GetMapping()
//    public ResponseEntity<?> getAllJobLevels() {
//        List<JobLevelDisplayModel> levels = jobLevelService.findAllJobLevels();
//        return ResponseEntity.ok(levels);
//    }
//
//    @GetMapping("/find/{id}")
//    public ResponseEntity<?> getJobLevelById(@PathVariable("id") Integer id) {
//        JobLevelDisplayModel jobLevel;
//        try {
//            jobLevel = jobLevelService.findJobLevelById(id);
//        } catch (ResourceNotFoundException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
//        }
//        return ResponseEntity.ok(jobLevel);
//    }
//
//    @GetMapping("/find/{name}")
//    public ResponseEntity<?> getJobLevelByName(@PathVariable("name") String name) {
//        List<JobLevelDisplayModel> jobLevel;
//        try {
//            jobLevel = jobLevelService.findByJobLevelName(name);
//        } catch (ResourceNotFoundException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
//        }
//        return ResponseEntity.ok(jobLevel);
//    }
//
//    @PostMapping("/add")
//    public ResponseEntity<?> registerJobLevel(@RequestBody JobLevelRequestModel request) {
//        try {
//            jobLevelService.addJobLevel(request);
//        } catch (ResourceAlreadyExistsException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
//        }
//        return ResponseEntity.ok().build();
//    }
//
//    @PutMapping("/update/{id}")
//    public ResponseEntity<?> updateJobLevel(@PathVariable("id") Integer id,
//                                          @RequestBody JobLevel request) {
//        try {
//            jobLevelService.updateJobLevel(id, request);
//        } catch (ResourceNotFoundException e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//        }
//        return ResponseEntity.ok().build();
//    }
//
//    @DeleteMapping("/delete/{id}")
//    public ResponseEntity deleteJobLevel(@PathVariable("id") Integer id) {
//        try {
//            jobLevelService.deleteJobLevel(id);
//        } catch (ResourceNotFoundException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
//        }
//        return ResponseEntity.ok().build();
//    }
//
//}
