package com.recruitmentsystem.controller;

import com.recruitmentsystem.dto.CategoryDto;
import com.recruitmentsystem.pagination.PageDto;
import com.recruitmentsystem.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/categories/all")
    public List<CategoryDto> getAllCategories() {
        return categoryService.findAllCategories();
    }

    @PostMapping("/categories/hot")
    public List<CategoryDto> getHotCategories(@RequestBody PageDto pageDto) {
        return categoryService.findCategoriesPagingAndSorting(pageDto);
    }

    @GetMapping("/admin/manage/categories/find/{categoryId}")
    public CategoryDto getCategoryById(@PathVariable("categoryId") Integer id) {
        return categoryService.findCategoryResponseModelById(id);
    }

    @PostMapping("/admin/manage/categories/add")
    @ResponseStatus(HttpStatus.CREATED)
    public void addCategory(@RequestBody CategoryDto request) {
        categoryService.addCategory(request);
    }

    @PutMapping("/admin/manage/categories/update/{categoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateCategory(@PathVariable("categoryId") Integer id,
                               @RequestBody CategoryDto request) {
        categoryService.updateCategory(id, request);
    }

    @DeleteMapping("/admin/manage/categories/delete/{categoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable("categoryId") Integer id) {
        categoryService.deleteCategory(id);
    }
}
