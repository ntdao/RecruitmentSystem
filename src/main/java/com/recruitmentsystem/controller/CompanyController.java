package com.recruitmentsystem.controller;

import com.recruitmentsystem.model.company.CompanyDisplayModel;
import com.recruitmentsystem.model.company.CompanyRequestModel;
import com.recruitmentsystem.service.impl.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/v1/companies")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN','HR')")
public class CompanyController {
    private final CompanyService companyService;

    @GetMapping("/all")
    public ResponseEntity<List<CompanyDisplayModel>> getAllCompanys() {
        List<CompanyDisplayModel> companies = companyService.findAllCompanies();
        return new ResponseEntity<>(companies, HttpStatus.OK);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<CompanyDisplayModel> getCompanyById(@PathVariable("id") Integer id) {
        CompanyDisplayModel company = companyService.findById(id);
        return new ResponseEntity<>(company, HttpStatus.OK);
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public void registerCompany(@RequestBody CompanyRequestModel request) {
        companyService.addCompany(request);
    }

    @PutMapping("/update/{id}")
    public void updateCompany(@PathVariable("id") Integer id, @RequestBody CompanyRequestModel request) {
        companyService.updateCompany(id, request);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteCompany(@PathVariable("id") Integer id) {
        companyService.deleteCompany(id);
    }

    @DeleteMapping("/delete")
    public void deleteCompany(@RequestBody Integer[] ids) {
        Stream.of(ids).forEach(id -> companyService.deleteCompany(id));
    }
}
