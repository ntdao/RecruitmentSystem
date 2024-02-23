package hust.seee.recruitmentsystem.controller;

import hust.seee.recruitmentsystem.dto.IndustryDTO;
import hust.seee.recruitmentsystem.response.BaseResponse;
import hust.seee.recruitmentsystem.service.IndustryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/industry")
@RequiredArgsConstructor
public class IndustryController {
    private final IndustryService industryService;

    @GetMapping("/all")
    public List<IndustryDTO> getAll() {
        return industryService.findAll();
    }

    @PostMapping("/save")
    public ResponseEntity<BaseResponse> save(@RequestBody IndustryDTO industry) {
        BaseResponse baseResponse = new BaseResponse();
        industryService.save(industry);

        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    @PostMapping("/get-by-id")
    public ResponseEntity<BaseResponse> findById(@RequestBody IndustryDTO dto) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setAdditionalProperty("data", industryService.findDTOById(dto.getId()));

        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    @PostMapping("/get-all")
    public ResponseEntity<BaseResponse> getAll(@RequestBody IndustryDTO industry) {
        BaseResponse baseResponse = new BaseResponse();
        Page<IndustryDTO> page = industryService.findAll(industry);
        baseResponse.setAdditionalProperty("data", page.getContent());
        baseResponse.setAdditionalProperty("total", page.getTotalElements());

        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    @PostMapping("/delete")
    public ResponseEntity<BaseResponse> delete(@RequestBody IndustryDTO dto) {
        BaseResponse baseResponse = new BaseResponse();
        industryService.delete(dto.getId());

        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }
}