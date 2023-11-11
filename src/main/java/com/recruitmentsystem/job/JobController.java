package com.recruitmentsystem.job;

import com.recruitmentsystem.address.AddressService;
import com.recruitmentsystem.address.Province.ProvinceResponseModel;
import com.recruitmentsystem.common.myEnum.Gender;
import com.recruitmentsystem.jobposition.JobPositionResponseModel;
import com.recruitmentsystem.jobposition.JobPositionService;
import com.recruitmentsystem.jobtype.JobTypeResponseModel;
import com.recruitmentsystem.jobtype.JobTypeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class JobController {
    private final AddressService addressService;
    private final JobService jobService;
    private final JobPositionService jobPositionService;
    private final JobTypeService jobTypeService;

    @GetMapping("/jobs/all")
    public List<JobResponseModel> getAllJobs() {
        return jobService.findAllJobs();
    }

    @GetMapping("/jobs/top")
    public List<JobResponseModel> getTopJobs(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "6") Integer pageSize,
            @RequestParam(defaultValue = "salary") String sortBy) {
        return jobService.getTopJobs(pageNo, pageSize, sortBy);
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
    public List<JobResponseModel>  getAllJobsAdmin() {
        return jobService.findAllJobsByAdmin();
    }

    @GetMapping("/admin/manage/jobs/find/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public JobResponseModel getJobById(@PathVariable("id") Integer id) {
        return jobService.findById(id);
    }

    @DeleteMapping("/admin/manage/jobs/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteJob(@PathVariable("id") Integer id,
                          HttpServletRequest request) {
        jobService.deleteJob(id, request.getUserPrincipal());
    }

//    @GetMapping("/hr/manage/jobs/all")
//    @PreAuthorize("hasRole('ROLE_HR')")
//    public ResponseEntity<?> getAllJobsHR(HttpServletRequest request) {
//        List<JobResponseModel> jobs;
//        try {
//            jobs = jobService.findAllJobsByHR(request.getUserPrincipal());
//        } catch (ResourceNotFoundException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
//        }
//        return ResponseEntity.ok(jobs);
//    }

    @GetMapping("/company/manage/jobs/find/{id}")
    @PreAuthorize("hasAuthority('COMPANY')")
    public JobResponseModel getJobByIdHR(@PathVariable("id") Integer id) {
        return jobService.findById(id);
    }

    @DeleteMapping("/company/manage/jobs/delete/{id}")
    @PreAuthorize("hasAuthority('COMPANY')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteJobByCompany(@PathVariable("id") Integer id,
                                   HttpServletRequest request) {
        jobService.deleteJob(id, request.getUserPrincipal());
    }


//    @PostMapping("/hr/manage/jobs/add")
//    @PreAuthorize("hasRole('ROLE_HR')")
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
