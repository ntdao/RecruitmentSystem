package hust.seee.recruitmentsystem.controller;

import hust.seee.recruitmentsystem.dto.AddressDTO;
import hust.seee.recruitmentsystem.dto.CompanyDTO;
import hust.seee.recruitmentsystem.response.BaseResponse;
import hust.seee.recruitmentsystem.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    public List<CompanyDTO> getAllCompanies() {
        return companyService.findAllCompanies();
    }

    @GetMapping("/companies/top")
    public List<CompanyDTO> getTopCompanies(@RequestParam(defaultValue = "0") Integer pageNo,
                                            @RequestParam(defaultValue = "6") Integer pageSize,
                                            @RequestParam(defaultValue = "companyId") String sortBy) {
        return companyService.getTopCompaniesModel(pageNo, pageSize, sortBy);
    }

    @GetMapping("/companies/find")
    public List<CompanyDTO> getCompanyByName(@RequestParam("name") String name) {
        return companyService.findCompanyByCompanyName(name);
    }

    @PostMapping("/companies/get-all")
    public ResponseEntity<BaseResponse> getCompany(@RequestBody CompanyDTO dto) {
        BaseResponse baseResponse = new BaseResponse();
        Page<CompanyDTO> page = companyService.getCompanyPaging(dto);
        baseResponse.setAdditionalProperty("data", page.getContent());
        baseResponse.setAdditionalProperty("total", page.getTotalElements());

        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    @GetMapping("/company/{companyId}")
    public CompanyDTO getCompanyByCompanyId(@PathVariable("companyId") Integer companyId) {
        return companyService.findCompanyByCompanyId(companyId);
    }

    @GetMapping("/admin/manage/companies/all")
    public List<CompanyDTO> getAllCompaniesAdmin() {
        return companyService.findAllCompanies();
    }

    @GetMapping("/admin/manage/companies/find/{id}")
    public CompanyDTO getCompanyById(@PathVariable("id") Integer id) {
        return companyService.findCompanyDTOById(id);
    }

    @PostMapping("/admin/manage/companies/add")
    @ResponseStatus(HttpStatus.CREATED)
    public CompanyDTO addCompany(@RequestBody CompanyDTO companyRequest) {
        return companyService.addCompanyByAdmin(companyRequest);
    }

//    @PutMapping("/admin/manage/companies/update")
//    public void updateCompany(@RequestBody CompanyDto dto) {
//        companyService.updateCompanyByAdmin(dto);
//    }

    @DeleteMapping("/admin/manage/companies/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompany(@PathVariable("id") Integer id) {
        companyService.deleteCompany(id);
    }

    @GetMapping("/company/manage/info")
    public CompanyDTO getCompanyInfo(Principal principal) {
        return companyService.findCompanyDisplayModel(principal);
    }

    @PostMapping("/company/manage/update-basic-info")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody CompanyDTO dto, Principal principal) {
        companyService.updateBasicInfoByCompany(dto, principal);
    }

    @PostMapping("/company/manage/update-desc")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateDesc(@RequestBody CompanyDTO dto, Principal principal) {
        companyService.updateDescByCompany(dto, principal);
    }

    @PostMapping("/company/manage/update-media")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateContact(@RequestBody CompanyDTO dto, Principal principal) {
        companyService.updateContactByCompany(dto, principal);
    }

    @PostMapping("/company/manage/update-address")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateCompanyAddress(@RequestBody AddressDTO addressRequestModel, Principal principal) {
        companyService.updateCompanyAddressByCompany(addressRequestModel, principal);
    }

    @PostMapping(
            value = "/company/manage/logo",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public String uploadCompanyLogo(@RequestParam("file") MultipartFile file, Principal principal) {
        return companyService.uploadCompanyImage(principal, file, "company-logo");
    }

    @PostMapping(
            value = "/company/manage/image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public String uploadCompanyImage(@RequestParam("file") MultipartFile file, Principal principal) {
        return companyService.uploadCompanyImage(principal, file, "company-images");
    }

    @PostMapping(
            value = "/company/manage/license",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public String uploadCompanyLicense(@RequestParam("file") MultipartFile file, Principal principal) {
        return companyService.uploadCompanyImage(principal, file, "company-license");
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
