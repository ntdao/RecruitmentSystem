package com.recruitmentsystem.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recruitmentsystem.dto.CategoryDto;
import com.recruitmentsystem.entity.Category;
import com.recruitmentsystem.exception.ResourceAlreadyExistsException;
import com.recruitmentsystem.exception.ResourceNotFoundException;
import com.recruitmentsystem.pagination.PageDto;
import com.recruitmentsystem.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final ObjectMapper objectMapper;
    private final CategoryRepository categoryRepository;

    private void checkDuplicatedCategoryName(String name) {
        if (categoryRepository.existsCategoryByCategoryName(name)) {
            throw new ResourceAlreadyExistsException("Category name already taken");
        }
    }

    @Transactional
    public void addCategory(CategoryDto request) {
        checkDuplicatedCategoryName(request.categoryName());
        Category category = objectMapper.convertValue(request, Category.class);
        categoryRepository.save(category);
    }

    public List<CategoryDto> findAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(c -> objectMapper.convertValue(c, CategoryDto.class))
                .collect(Collectors.toList());
    }

    public Category findById(Integer id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category with id " + id + " does not exist"));
    }

    public Category findCategoryByName(String name) {
        return categoryRepository.findByCategoryName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Category with name " + name + " does not exist"));
    }

    public List<Category> findByName(String name) {
        return categoryRepository.findByCategoryNameContains(name);
    }

    public CategoryDto findCategoryResponseModelById(Integer id) {
        return objectMapper.convertValue(findById(id), CategoryDto.class);
    }

    @Transactional
    public void updateCategory(Integer id, CategoryDto request) {
        Category updateCategory = findById(id);
        Category categoryRequest = objectMapper.convertValue(request, Category.class);
        categoryRequest.setCategoryId(id);
        categoryRepository.save(updateCategory);
    }

    public void deleteCategory(Integer id) {
        Category category = findById(id);
        categoryRepository.delete(category);
    }

    public List<CategoryDto> findCategoriesPagingAndSorting(PageDto pageDto) {
        Pageable paging = PageRequest.of(
                pageDto.getPageNo(),
                pageDto.getPageSize(),
                Sort.Direction.fromString(pageDto.getSortDir()),
                pageDto.getSortBy());

        Page<Category> pagedResult = categoryRepository.findAll(paging);

        if (pagedResult.hasContent()) {
            return pagedResult.getContent()
                    .stream()
                    .map(c -> objectMapper.convertValue(c, CategoryDto.class))
                    .collect(Collectors.toList());
        } else {
            return null;
        }
    }
}
