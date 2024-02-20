package com.recruitmentsystem.controller;

import com.recruitmentsystem.dto.DegreeDTO;
import com.recruitmentsystem.response.BaseResponse;
import com.recruitmentsystem.service.DegreeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/degree")
@RequiredArgsConstructor
public class DegreeController {
    private final DegreeService degreeService;

    @PostMapping("/save")
    public ResponseEntity<BaseResponse> save(@RequestBody DegreeDTO degree) {
        BaseResponse baseResponse = new BaseResponse();
        degreeService.save(degree);

        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    @PostMapping("/get-by-id")
    public ResponseEntity<BaseResponse> findById(@RequestBody DegreeDTO dto) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setAdditionalProperty("data", degreeService.findDTOById(dto.getId()));

        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    @PostMapping("/get-all")
    public ResponseEntity<BaseResponse> getAll(@RequestBody DegreeDTO degree) {
        BaseResponse baseResponse = new BaseResponse();
        Page<DegreeDTO> page = degreeService.findAll(degree);
        baseResponse.setAdditionalProperty("data", page.getContent());
        baseResponse.setAdditionalProperty("total", page.getTotalElements());

        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    @PostMapping("/delete")
    public ResponseEntity<BaseResponse> delete(@RequestBody DegreeDTO dto) {
        BaseResponse baseResponse = new BaseResponse();
        degreeService.delete(dto.getId());

        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }
}

