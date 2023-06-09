package com.recruitmentsystem.controller;

import com.recruitmentsystem.common.exception.ResourceAlreadyExistsException;
import com.recruitmentsystem.common.exception.ResourceNotFoundException;
import com.recruitmentsystem.entity.CompanyBranch;
import com.recruitmentsystem.model.branch.BranchDisplayModel;
import com.recruitmentsystem.model.branch.BranchRequestModel;
import com.recruitmentsystem.service.BranchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/branch/branches")
@RequiredArgsConstructor
public class BranchController {
    private final BranchService branchService;

    @GetMapping("/all")
    public ResponseEntity<List<BranchDisplayModel>> getAllBranches() {
        List<BranchDisplayModel> companies = branchService.findAllCompanies();
        return ResponseEntity.ok(companies);
    }

    @GetMapping("/find/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    public ResponseEntity<?> getBranchById(@PathVariable("id") Integer id) {
        BranchDisplayModel branch;
        try {
            branch = branchService.findById(id);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        return ResponseEntity.ok(branch);
    }

    @GetMapping("/find/{name}")
    public ResponseEntity<?> getBranchByName(@PathVariable("name") String name) {
        CompanyBranch branch;
        try {
            branch = branchService.findBranchByBranchName(name);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        return ResponseEntity.ok(branch);
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    public ResponseEntity<?> registerBranch(@RequestBody BranchRequestModel request) {
        try {
            branchService.addBranch(request);
        } catch (ResourceAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/update/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    public ResponseEntity<?> updateBranch(@PathVariable("id") Integer id,
                                          @RequestBody BranchRequestModel request) {
        try {
            branchService.updateBranch(id, request);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    public ResponseEntity deleteBranch(@PathVariable("id") Integer id) {
        try {
            branchService.deleteBranch(id);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }
}
