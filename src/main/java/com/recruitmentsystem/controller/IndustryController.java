package com.recruitmentsystem.controller;

import com.recruitmentsystem.dto.IndustryDto;
import com.recruitmentsystem.service.IndustryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class IndustryController {
    private final IndustryService industryService;

    @GetMapping("/industries/all")
    public List<IndustryDto> getAllIndustries() {
        return industryService.findAllIndustryResponseModel();
    }

    @GetMapping("/admin/manage/industries/find/{industryId}")
    public IndustryDto getIndustryById(@PathVariable("industryId") Integer id) {
        return industryService.findIndustryResponseModelById(id);
    }

    @PostMapping("/admin/manage/industries/add")
    @ResponseStatus(HttpStatus.CREATED)
    public void addIndustry(@RequestBody IndustryDto request) {
        industryService.addIndustry(request);
    }

    @PutMapping("/admin/manage/industries/update/{industryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateIndustry(@PathVariable("industryId") Integer id,
                               @RequestBody IndustryDto request) {
        industryService.updateIndustry(id, request);
    }

    @DeleteMapping("/admin/manage/industries/delete/{industryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteIndustry(@PathVariable("industryId") Integer id) {
        industryService.deleteIndustry(id);
    }
}