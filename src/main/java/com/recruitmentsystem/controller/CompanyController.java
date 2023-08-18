package com.recruitmentsystem.controller;

import com.recruitmentsystem.common.exception.ResourceAlreadyExistsException;
import com.recruitmentsystem.common.exception.ResourceNotFoundException;
import com.recruitmentsystem.entity.Company;
import com.recruitmentsystem.entity.Job;
import com.recruitmentsystem.model.company.CompanyDisplayModel;
import com.recruitmentsystem.model.company.CompanyRequestModel;
import com.recruitmentsystem.service.CompanyService;
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

    @GetMapping("/companies/jobs/{companyId}")
    public ResponseEntity<?> getAllJob(@PathVariable("companyId") Integer id) {
        try {
            List<Job> jobs = jobService.findAllJobsByCompany(id);
            return ResponseEntity.ok(jobs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/manage_companies/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<Company>> getAllCompaniesAdmin() {
        List<Company> companies = companyService.findAllCompaniesAdmin();
        return ResponseEntity.ok(companies);
    }

    @GetMapping("/manage_companies/find/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getCompanyById(@PathVariable("id") Integer id) {
        Company company;
        try {
            company = companyService.findCompanyByIdAdmin(id);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        return ResponseEntity.ok(company);
    }


    @PostMapping("/manage_companies/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> addCompany(@RequestBody CompanyRequestModel request) {
        try {
            companyService.addCompanyAdmin(request);
        } catch (ResourceAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    @PutMapping("/manage_companies/update/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateCompany(@PathVariable("id") Integer id,
                                           @RequestBody CompanyRequestModel request) {
        try {
            companyService.updateCompanyByAdmin(id, request);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/manage_companies/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity deleteCompany(@PathVariable("id") Integer id) {
        try {
            companyService.deleteCompany(id);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }
}
