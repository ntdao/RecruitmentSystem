package com.recruitmentsystem.controller;

import com.recruitmentsystem.dto.SkillDto;
import com.recruitmentsystem.pagination.PageDto;
import com.recruitmentsystem.service.SkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/skills")
@RequiredArgsConstructor
public class SkillController {
    private final SkillService skillService;

    @GetMapping("/all")
    public List<SkillDto> getAllSkills() {
        return skillService.findAllSkillDto();
    }

    @PostMapping("/paging")
    public List<SkillDto> getSkillsPaging(@RequestBody PageDto pageDto) {
        return skillService.findSkillsByName(pageDto);
    }
}
