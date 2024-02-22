package hust.seee.recruitmentsystem.controller;

import hust.seee.recruitmentsystem.dto.SkillDTO;
import hust.seee.recruitmentsystem.response.BaseResponse;
import hust.seee.recruitmentsystem.service.SkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/skill")
@RequiredArgsConstructor
public class SkillController {
    private final SkillService skillService;

    @GetMapping("/all")
    public List<SkillDTO> getAll() {
        return skillService.findAll();
    }

    @PostMapping("/save")
    public ResponseEntity<BaseResponse> save(@RequestBody SkillDTO skill) {
        BaseResponse baseResponse = new BaseResponse();
        skillService.save(skill);

        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    @PostMapping("/get-by-id")
    public ResponseEntity<BaseResponse> findById(@RequestBody SkillDTO dto) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setAdditionalProperty("data", skillService.findDTOById(dto.getId()));

        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    @PostMapping("/get-all")
    public ResponseEntity<BaseResponse> getAll(@RequestBody SkillDTO skill) {
        BaseResponse baseResponse = new BaseResponse();
        Page<SkillDTO> page = skillService.findAll(skill);
        baseResponse.setAdditionalProperty("data", page.getContent());
        baseResponse.setAdditionalProperty("total", page.getTotalElements());

        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    @PostMapping("/delete")
    public ResponseEntity<BaseResponse> delete(@RequestBody SkillDTO dto) {
        BaseResponse baseResponse = new BaseResponse();
        skillService.delete(dto.getId());

        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }
}