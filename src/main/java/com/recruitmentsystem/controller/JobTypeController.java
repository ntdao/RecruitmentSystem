package com.recruitmentsystem.controller;

import com.recruitmentsystem.dto.JobTypeDTO;
import com.recruitmentsystem.response.BaseResponse;
import com.recruitmentsystem.service.JobTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/job-type")
@RequiredArgsConstructor
public class JobTypeController {
    private final JobTypeService jobTypeService;

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

