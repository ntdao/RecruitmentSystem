package hust.seee.recruitmentsystem.controller;

import hust.seee.recruitmentsystem.dto.JobTypeDTO;
import hust.seee.recruitmentsystem.response.BaseResponse;
import hust.seee.recruitmentsystem.service.JobTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/job-type")
@RequiredArgsConstructor
public class JobTypeController {
    private final JobTypeService jobTypeService;

    @GetMapping("/all")
    public List<JobTypeDTO> getAllIndustries() {
        return jobTypeService.findAll();
    }

    @PostMapping("/save")
    public ResponseEntity<BaseResponse> save(@RequestBody JobTypeDTO jobType) {
        BaseResponse baseResponse = new BaseResponse();
        jobTypeService.save(jobType);

        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    @PostMapping("/get-by-id")
    public ResponseEntity<BaseResponse> findById(@RequestBody JobTypeDTO dto) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setAdditionalProperty("data", jobTypeService.findDTOById(dto.getId()));

        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    @PostMapping("/get-all")
    public ResponseEntity<BaseResponse> getAll(@RequestBody JobTypeDTO jobType) {
        BaseResponse baseResponse = new BaseResponse();
        Page<JobTypeDTO> page = jobTypeService.findAll(jobType);
        baseResponse.setAdditionalProperty("data", page.getContent());
        baseResponse.setAdditionalProperty("total", page.getTotalElements());

        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    @PostMapping("/delete")
    public ResponseEntity<BaseResponse> delete(@RequestBody JobTypeDTO dto) {
        BaseResponse baseResponse = new BaseResponse();
        jobTypeService.delete(dto.getId());

        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }
}

