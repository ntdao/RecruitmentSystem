package com.recruitmentsystem.category;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/categories/all")
    public List<CategoryResponseModel> getAllCategories() {
        return categoryService.findAllCategories();
    }

    @GetMapping("/categories/hot")
    public List<CategoryResponseModel> getHotCategories(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "20") Integer pageSize,
            @RequestParam(defaultValue = "total") String sortBy
    ) {
        return categoryService.findHotCategories(pageNo, pageSize, sortBy);
    }

    @GetMapping("/admin/manage/categories/find/{categoryId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public CategoryResponseModel getCategoryById(@PathVariable("categoryId") Integer id) {
        return categoryService.findCategoryResponseModelById(id);
    }

    @PostMapping("/admin/manage/categories/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public void addCategory(@RequestBody CategoryRequestModel request) {
        categoryService.addCategory(request);
    }

    @PutMapping("/admin/manage/categories/update/{categoryId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateCategory(@PathVariable("categoryId") Integer id,
                               @RequestBody CategoryRequestModel request) {
        categoryService.updateCategory(id, request);
    }

    @DeleteMapping("/admin/manage/categories/delete/{categoryId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable("categoryId") Integer id) {
        categoryService.deleteCategory(id);
    }
}
