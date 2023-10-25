package com.recruitmentsystem.controller;

import com.recruitmentsystem.common.exception.ResourceAlreadyExistsException;
import com.recruitmentsystem.common.exception.ResourceNotFoundException;
import com.recruitmentsystem.entity.Company;
import com.recruitmentsystem.model.company.CompanyDisplayModel;
import com.recruitmentsystem.model.company.CompanyRequestModel;
import com.recruitmentsystem.model.job.JobDisplayModel;
import com.recruitmentsystem.service.CompanyService;
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
public class CompanyController {
    private final CompanyService companyService;
    private final JobService jobService;

    @GetMapping("/companies/all")
    public ResponseEntity<List<CompanyDisplayModel>> getAllCompanies() {
        List<CompanyDisplayModel> companies = companyService.findAllCompanies();
        return ResponseEntity.ok(companies);
    }

    @GetMapping("/companies/top")
    public ResponseEntity<?> getTopCompanies(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "6") Integer pageSize,
            @RequestParam(defaultValue = "companyId") String sortBy) {
        List<CompanyDisplayModel> list = companyService.getTopCompanies(pageNo, pageSize, sortBy);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/companies/find/{name}")
    public ResponseEntity<?> getCompanyByName(@PathVariable("name") String name) {
        List<CompanyDisplayModel> companies = companyService.findCompanyByCompanyName(name);
        return ResponseEntity.ok(companies);
    }

    @GetMapping("/companies/jobs/{company-id}")
    public ResponseEntity<?> getAllJob(@PathVariable("company-id") Integer id) {
        try {
            List<JobDisplayModel> jobs = jobService.findAllJobsByCompany(id);
            return ResponseEntity.ok(jobs);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/manage/companies/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<CompanyDisplayModel>> getAllCompaniesAdmin() {
        List<CompanyDisplayModel> companies = companyService.findAllCompanies();
        return ResponseEntity.ok(companies);
    }

    @GetMapping("/manage/companies/find/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getCompanyById(@PathVariable("id") Integer id) {
        Company company;
        try {
            company = companyService.findCompanyById(id);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        return ResponseEntity.ok(company);
    }


    @PostMapping("/manage/companies/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> addCompany(@RequestBody CompanyRequestModel companyRequest,
                                        HttpServletRequest request) {
        try {
            companyService.addCompanyAdmin(companyRequest, request.getUserPrincipal());
        } catch (ResourceAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    @PutMapping("/manage/companies/update/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateCompany(@PathVariable("id") Integer id,
                                           @RequestBody CompanyRequestModel companyRequest,
                                           HttpServletRequest request) {
        try {
            companyService.updateCompanyByAdmin(id, companyRequest, request.getUserPrincipal());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/manage/companies/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity deleteCompany(@PathVariable("id") Integer id,
                                        HttpServletRequest request) {
        try {
            companyService.deleteCompany(id, request.getUserPrincipal());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }
}
