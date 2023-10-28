package com.recruitmentsystem.controller;

import com.recruitmentsystem.common.exception.ResourceAlreadyExistsException;
import com.recruitmentsystem.common.exception.ResourceNotFoundException;
import com.recruitmentsystem.entity.CompanyBranch;
import com.recruitmentsystem.model.branch.BranchRequestModel;
import com.recruitmentsystem.model.job.JobDisplayModel;
import com.recruitmentsystem.service.BranchService;
import com.recruitmentsystem.service.JobService;
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
public class BranchController {
    private final BranchService branchService;
    private final JobService jobService;

    @GetMapping("/branches/{company-id}")
    public ResponseEntity<?> getAllBranchesByCompany(@PathVariable("company-id") Integer companyId) {
        List<CompanyBranch> companies = branchService.findAllBranchesByCompany(companyId);
        return ResponseEntity.ok(companies);
    }

//    @GetMapping("/branches/find/{name}")
//    public ResponseEntity<?> getBranchByName(@PathVariable("name") String name) {
//        List<BranchDisplayModel> branch;
//        try {
//            branch = branchService.findBranchByBranchName(name);
//        } catch (ResourceNotFoundException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
//        }
//        return ResponseEntity.ok(branch);
//    }

    @GetMapping("/branches/jobs/{branch-id}")
    public ResponseEntity<?> getAllJob(@PathVariable("branch-id") Integer id) {
        try {
            List<JobDisplayModel> jobs = jobService.findAllJobsByBranch(id);
            return ResponseEntity.ok(jobs);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

//    @GetMapping("/manage/branches/all")
//    public ResponseEntity<?> getAllBranches() {
//        List<CompanyBranch> companies = branchService.findAllBranchesAdmin();
//        return ResponseEntity.ok(companies);
//    }

    @GetMapping("/manage/branches/find/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getBranchById(@PathVariable("id") Integer id) {
        CompanyBranch branch;
        try {
            branch = branchService.findByIdAdmin(id);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        return ResponseEntity.ok(branch);
    }

    @GetMapping("/manage/branches/find/{name}")
    public ResponseEntity<?> getBranchByNameAdmin(@PathVariable("name") String name) {
        List<CompanyBranch> branch;
        try {
            branch = branchService.findBranchByBranchNameAdmin(name);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        return ResponseEntity.ok(branch);
    }

    @PostMapping("/manage/branches/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> addBranch(@RequestBody BranchRequestModel branchRequest,
                                       HttpServletRequest request) {
        try {
            branchService.addBranch(branchRequest, request.getUserPrincipal());
        } catch (ResourceAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    @PutMapping("/manage/branches/update/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateBranch(@PathVariable("id") Integer id,
                                          @RequestBody BranchRequestModel branchRequest,
                                          HttpServletRequest request) {
        try {
            branchService.updateBranch(id, branchRequest, request.getUserPrincipal());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/manage/branches/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity deleteBranch(@PathVariable("id") Integer id,
                                       HttpServletRequest request) {
        try {
            branchService.deleteBranch(id, request.getUserPrincipal());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }
}
