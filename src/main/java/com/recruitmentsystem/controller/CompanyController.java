package com.recruitmentsystem.controller;

import com.recruitmentsystem.dto.*;
import com.recruitmentsystem.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CompanyController {
    private final CompanyService companyService;

    @GetMapping("/companies/all")
    public List<CompanyResponseModel> getAllCompanies() {
        return companyService.findAllCompanies();
    }

    @GetMapping("/companies/top")
    public List<CompanyTopModel> getTopCompanies(@RequestParam(defaultValue = "0") Integer pageNo,
                                                 @RequestParam(defaultValue = "6") Integer pageSize,
                                                 @RequestParam(defaultValue = "companyId") String sortBy) {
        return companyService.getTopCompaniesModel(pageNo, pageSize, sortBy);
    }

    @GetMapping("/companies/find")
    public List<CompanyResponseModel> getCompanyByName(@RequestParam("name") String name) {
        return companyService.findCompanyByCompanyName(name);
    }

    @GetMapping("/admin/manage/companies/all")
    public List<CompanyResponseModel> getAllCompaniesAdmin() {
        return companyService.findAllCompanies();
    }

    @GetMapping("/admin/manage/companies/find/{id}")
    public CompanyResponseModel getCompanyById(@PathVariable("id") Integer id) {
        return companyService.findCompanyResponseModelById(id);
    }

    @PostMapping("/admin/manage/companies/add")
    @ResponseStatus(HttpStatus.CREATED)
    public CompanyResponseModel addCompany(@RequestBody CompanyRequestModel companyRequest) {
        return companyService.addCompanyByAdmin(companyRequest);
    }

//    @PutMapping("/admin/manage/companies/update/{id}")
//    @PreAuthorize("hasAuthority('ADMIN')")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public void updateCompany(@PathVariable("id") Integer id,
//                              @RequestBody CompanyRequestModel companyRequest) {
//        companyService.updateCompanyByAdmin(id, companyRequest);
//    }

    @DeleteMapping("/admin/manage/companies/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompany(@PathVariable("id") Integer id) {
        companyService.deleteCompany(id);
    }

    @GetMapping("/company/manage/info")
    public CompanyResponseModel getCompanyInfo(Principal connectedUser) {
        return companyService.findCompanyDisplayModel(connectedUser);
    }

    @PostMapping("/company/manage/update-basic-info")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody CompanyRequestModel companyRequestModel,
                       Principal connectedUser) {
        companyService.updateBasicInfoByCompany(companyRequestModel, connectedUser);
    }

    @PostMapping("/company/manage/update-desc")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateDesc(@RequestBody CompanyRequestModel companyRequestModel,
                           Principal connectedUser) {
        companyService.updateDescByCompany(companyRequestModel, connectedUser);
    }

    @PostMapping("/company/manage/update-contact")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateContact(@RequestBody CompanyRequestModel companyRequestModel,
                              Principal connectedUser) {
        companyService.updateContactByCompany(companyRequestModel, connectedUser);
    }

    @PostMapping("/company/manage/update-address")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateCompanyAddress(@RequestBody AddressDto addressRequestModel,
                                     Principal connectedUser) {
        companyService.updateCompanyAddressByCompany(addressRequestModel, connectedUser);
    }

    @PostMapping(
            value = "/company/manage/logo",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public String uploadCompanyLogo(@RequestParam("file") MultipartFile[] files,
                                    Principal connectedUser) {
        return companyService.uploadCompanyImage(connectedUser, files, "company-logo");
    }

    @PostMapping(
            value = "/company/manage/image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public String uploadCompanyImage(@RequestParam("file") MultipartFile[] files,
                                     Principal connectedUser) {
        return companyService.uploadCompanyImage(connectedUser, files, "company-images");
    }

    @PostMapping(
            value = "/company/manage/license",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public String uploadCompanyLicense(@RequestParam("file") MultipartFile[] files,
                                       Principal connectedUser) {
        return companyService.uploadCompanyImage(connectedUser, files, "company-license");
    }

    @GetMapping(
            value = "/company/{companyId}/logo",
            produces = MediaType.IMAGE_JPEG_VALUE
    )
    public byte[] getCompanyLogo(@PathVariable("companyId") Integer companyId) {
        return companyService.getCompanyImage(companyId, "company-logo");
    }

    @GetMapping(
            value = "/company/{companyId}/image",
            produces = MediaType.IMAGE_JPEG_VALUE
    )
    public byte[] getCompanyImage(@PathVariable("companyId") Integer companyId) {
        return companyService.getCompanyImage(companyId, "company-images");
    }

    @GetMapping(
            value = "/company/{companyId}/license",
            produces = MediaType.IMAGE_JPEG_VALUE
    )
    public byte[] getCompanyLicense(@PathVariable("companyId") Integer companyId) {
        return companyService.getCompanyImage(companyId, "company-license");
    }
}
