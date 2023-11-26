package com.recruitmentsystem.company;

import com.recruitmentsystem.job.JobResponseModel;
import com.recruitmentsystem.job.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CompanyController {
    private final CompanyService companyService;
    private final JobService jobService;

    @GetMapping("/companies/all")
    public List<CompanyResponseModel> getAllCompanies() {
        return companyService.findAllCompanies();
    }

    // Lỗi lazyInit...
    @GetMapping("/companies/top")
    public List<CompanyTopModel> getTopCompanies(@RequestParam(defaultValue = "0") Integer pageNo,
                                                      @RequestParam(defaultValue = "6") Integer pageSize,
                                                      @RequestParam(defaultValue = "companyId") String sortBy) {
        return companyService.getTopCompaniesModel(pageNo, pageSize, sortBy);
    }

    @GetMapping("/companies/find/{name}")
    public List<CompanyResponseModel> getCompanyByName(@PathVariable("name") String name) {
        return companyService.findCompanyByCompanyName(name);
    }

    @GetMapping("/companies/{company-id}/jobs")
    public List<JobResponseModel> getAllJob(@PathVariable("company-id") Integer id) {
        return jobService.findJobByCompanyId(id);
    }

    @GetMapping("/admin/manage/companies/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<Company> getAllCompaniesAdmin() {
        return companyService.findAllCompaniesAdmin();
    }

    @GetMapping("/admin/manage/companies/find/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public CompanyResponseModel getCompanyById(@PathVariable("id") Integer id) {
        return companyService.findCompanyResponseModelById(id);
    }

    @PostMapping("/admin/manage/companies/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public CompanyResponseModel addCompany(@RequestBody CompanyRequestModel companyRequest) {
        return companyService.addCompanyByAdmin(companyRequest);
    }

    @PutMapping("/admin/manage/companies/update/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateCompany(@PathVariable("id") Integer id,
                              @RequestBody CompanyRequestModel companyRequest) {
        companyService.updateCompanyByAdmin(id, companyRequest);
    }

    @DeleteMapping("/admin/manage/companies/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompany(@PathVariable("id") Integer id) {
        companyService.deleteCompany(id);
    }

    @GetMapping("/company/manage/info")
    @PreAuthorize("hasAuthority('COMPANY')")
    public CompanyResponseModel getCompanyInfo(Principal connectedUser) {
        return companyService.findCompanyDisplayModel(connectedUser);
    }

    @GetMapping("/company/manage/update")
    @PreAuthorize("hasAuthority('COMPANY')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody CompanyRequestModel companyRequestModel,
                                       Principal connectedUser) {
        companyService.updateCompanyByCompany(companyRequestModel, connectedUser);
    }
}
