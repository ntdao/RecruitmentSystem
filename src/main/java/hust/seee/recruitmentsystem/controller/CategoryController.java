package hust.seee.recruitmentsystem.controller;

import hust.seee.recruitmentsystem.dto.CategoryDTO;
import hust.seee.recruitmentsystem.pagination.PageDto;
import hust.seee.recruitmentsystem.response.BaseResponse;
import hust.seee.recruitmentsystem.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/all")
    public List<CategoryDTO> getAllCategories() {
        return categoryService.findAll();
    }

    @PostMapping("/hot")
    public List<CategoryDTO> getHotCategories(@RequestBody PageDto pageDto) {
        return categoryService.findPaging(pageDto);
    }

    @PostMapping("/save")
    public ResponseEntity<BaseResponse> save(@RequestBody CategoryDTO category) {
        BaseResponse baseResponse = new BaseResponse();
        categoryService.save(category);

        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    @PostMapping("/get-by-id")
    public ResponseEntity<BaseResponse> findById(@RequestBody CategoryDTO dto) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setAdditionalProperty("data", categoryService.findDTOById(dto.getId()));

        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    @PostMapping("/get-all")
    public ResponseEntity<BaseResponse> getAll(@RequestBody CategoryDTO category) {
        BaseResponse baseResponse = new BaseResponse();
        Page<CategoryDTO> page = categoryService.findAll(category);
        baseResponse.setAdditionalProperty("data", page.getContent());
        baseResponse.setAdditionalProperty("total", page.getTotalElements());

        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    @PostMapping("/delete")
    public ResponseEntity<BaseResponse> delete(@RequestBody CategoryDTO dto) {
        BaseResponse baseResponse = new BaseResponse();
        categoryService.delete(dto.getId());

        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }
}