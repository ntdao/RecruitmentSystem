package com.recruitmentsystem.controller;

import com.recruitmentsystem.dto.AddressDto;
import com.recruitmentsystem.dto.CompanyDto;
import com.recruitmentsystem.dto.CompanyResponseModel;
import com.recruitmentsystem.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;
import java.util.Map;

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
    public List<Map<String, Object>> getTopCompanies(@RequestParam(defaultValue = "0") Integer pageNo,
                                                     @RequestParam(defaultValue = "6") Integer pageSize,
                                                     @RequestParam(defaultValue = "companyId") String sortBy) {
        return companyService.getTopCompaniesModel(pageNo, pageSize, sortBy);
    }

    @GetMapping("/companies/find")
    public List<CompanyResponseModel> getCompanyByName(@RequestParam("name") String name) {
        return companyService.findCompanyByCompanyName(name);
    }

    @GetMapping("/company/{companyId}")
    public CompanyResponseModel getCompanyByCompanyId(@PathVariable("companyId") Integer companyId) {
        return companyService.findCompanyByCompanyId(companyId);
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
    public CompanyResponseModel addCompany(@RequestBody CompanyDto companyRequest) {
        return companyService.addCompanyByAdmin(companyRequest);
    }

//    @PutMapping("/admin/manage/companies/update/{id}")
//    @PreAuthorize("hasAuthority('ADMIN')")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public void updateCompany(@PathVariable("id") Integer id,
//                              @RequestBody CompanyDto companyRequest) {
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
    public void update(@RequestBody CompanyDto dto,
                       Principal connectedUser) {
        companyService.updateBasicInfoByCompany(dto, connectedUser);
    }

    @PostMapping("/company/manage/update-desc")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateDesc(@RequestBody CompanyDto dto,
                           Principal connectedUser) {
        companyService.updateDescByCompany(dto, connectedUser);
    }

    @PostMapping("/company/manage/update-media")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateContact(@RequestBody CompanyDto dto,
                              Principal connectedUser) {
        companyService.updateContactByCompany(dto, connectedUser);
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
    public String uploadCompanyLogo(@RequestParam("file") MultipartFile file,
                                    Principal connectedUser) {
        return companyService.uploadCompanyImage(connectedUser, file, "company-logo");
    }

    @PostMapping(
            value = "/company/manage/image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public String uploadCompanyImage(@RequestParam("file") MultipartFile file,
                                     Principal connectedUser) {
        return companyService.uploadCompanyImage(connectedUser, file, "company-images");
    }

    @PostMapping(
            value = "/company/manage/license",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public String uploadCompanyLicense(@RequestParam("file") MultipartFile file,
                                       Principal connectedUser) {
        return companyService.uploadCompanyImage(connectedUser, file, "company-license");
    }

//    @GetMapping(
//            value = "/company/{companyId}/logo",
//            produces = MediaType.IMAGE_JPEG_VALUE
//    )
//    public byte[] getCompanyLogo(@PathVariable("companyId") Integer companyId) {
//        return companyService.getCompanyImage(companyId, "company-logo").get(0);
//    }
//
//    @GetMapping(
//            value = "/company/{companyId}/image",
//            produces = MediaType.IMAGE_JPEG_VALUE
//    )
//    public List<byte[]> getCompanyImage(@PathVariable("companyId") Integer companyId) {
//        return companyService.getCompanyImage(companyId, "company-images");
//    }
//
//    @GetMapping(
//            value = "/company/{companyId}/license/{index}",
//            produces = MediaType.IMAGE_JPEG_VALUE
//    )
//    public byte[] getCompanyLicense(@PathVariable("companyId") Integer companyId,
//                                          @PathVariable("index") Integer index) {
//        return companyService.getCompanyImage(companyId, "company-license").get(index);
//    }
}
