package com.recruitmentsystem.industry;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class IndustryController {

    private final IndustryService industryService;

    @GetMapping("/industries/all")
    public List<IndustryResponseModel> getAllCategories() {
        return industryService.findAllIndustryResponseModel();
    }

    @GetMapping("/admin/manage/industries/find/{industryId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public IndustryResponseModel getIndustryById(@PathVariable("industryId") Integer id) {
        return industryService.findIndustryResponseModelById(id);
    }

    @PostMapping("/admin/manage/industries/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public void addIndustry(@RequestBody IndustryRequestModel request) {
        industryService.addIndustry(request);
    }

    @PutMapping("/admin/manage/industries/update/{industryId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateIndustry(@PathVariable("industryId") Integer id,
                               @RequestBody IndustryRequestModel request) {
        industryService.updateIndustry(id, request);
    }

    @DeleteMapping("/admin/manage/industries/delete/{industryId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteIndustry(@PathVariable("industryId") Integer id) {
        industryService.deleteIndustry(id);
    }
}