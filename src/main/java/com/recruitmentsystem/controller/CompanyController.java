package com.recruitmentsystem.controller;

import com.recruitmentsystem.common.exception.ResourceAlreadyExistsException;
import com.recruitmentsystem.common.exception.ResourceNotFoundException;
import com.recruitmentsystem.model.company.CompanyDisplayModel;
import com.recruitmentsystem.model.company.CompanyRequestModel;
import com.recruitmentsystem.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/companies")
@RequiredArgsConstructor
public class CompanyController {
    private final CompanyService companyService;

    @GetMapping
    public ResponseEntity<List<CompanyDisplayModel>> getAllCompanies() {
        List<CompanyDisplayModel> companies = companyService.findAllCompanies();
        return ResponseEntity.ok(companies);
    }
    @GetMapping("/top")
    public ResponseEntity<?> getTopCompanies(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "6") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy)
    {
        List<CompanyDisplayModel> list = companyService.getTopCompanies(pageNo, pageSize, sortBy);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/find/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    public ResponseEntity<?> getCompanyById(@PathVariable("id") Integer id) {
        CompanyDisplayModel company;
        try {
            company = companyService.findById(id);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        return ResponseEntity.ok(company);
    }

    @GetMapping("/find/{name}")
    public ResponseEntity<?> getCompanyByName(@PathVariable("name") String name) {
        List<CompanyDisplayModel> companies = companyService.findCompanyByCompanyName(name);
        return ResponseEntity.ok(companies);
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    public ResponseEntity<?> registerCompany(@RequestBody CompanyRequestModel request) {
        try {
            companyService.addCompany(request);
        } catch (ResourceAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/update/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    public ResponseEntity<?> updateCompany(@PathVariable("id") Integer id,
                                           @RequestBody CompanyRequestModel request) {
        try {
            companyService.updateCompany(id, request);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    public ResponseEntity deleteCompany(@PathVariable("id") Integer id) {
        try {
            companyService.deleteCompany(id);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }
}
