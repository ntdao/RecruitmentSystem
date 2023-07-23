package com.recruitmentsystem.service;

import com.recruitmentsystem.common.exception.ResourceAlreadyExistsException;
import com.recruitmentsystem.common.exception.ResourceNotFoundException;
import com.recruitmentsystem.common.myEnum.OrderBy;
import com.recruitmentsystem.entity.Category;
import com.recruitmentsystem.repository.ICategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final ICategoryRepository categoryRepository;
    
    public void addCategory(Category request) {
        String name = request.getCategoryName();
        if (categoryRepository.existsCategoryByCategoryName(name)) {
            throw new ResourceAlreadyExistsException("Category name already taken");
        }

        request.setCreatedAt(Instant.now());
//        category.setCreatedBy(getCategoryByToken(request.token()).getId());
        categoryRepository.save(request);
    }

    public List<Category> findAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .filter(category -> !category.isDeleteFlag())
                .collect(Collectors.toList());
    }

    public Category findById(Integer id) {
        return categoryRepository.findById(id)
                .filter(category -> !category.isDeleteFlag())
                .orElseThrow(() -> new ResourceNotFoundException("Category with id " + id + " does not exist"));
    }

    @Transactional
    public void updateCategory(Integer id, Category request) throws IOException {
        Category updateCategory;
        try {
            updateCategory = findById(id);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Category with id " + id + " does not exist");
        }
//        updateCategory.setUpdatedAt(Instant.now());
        Category oldCategory = new Category(updateCategory, id, true);
        categoryRepository.save(oldCategory);

        // update category
        updateCategory.setCategoryId(id);
        updateCategory.setCreatedAt(oldCategory.getCreatedAt());
        updateCategory.setCreatedBy(oldCategory.getCreatedBy());
        updateCategory.setUpdatedAt(oldCategory.getUpdatedAt());
//        updateCategory.setUpdatedBy(getCategoryByToken(request.token()).getId());
        categoryRepository.save(updateCategory);
    }

    public void deleteCategory(Integer id) {
        Category category;
        try {
            category = findById(id);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Category with id " + id + " does not exist");
        }
        category.setDeleteFlag(true);
        categoryRepository.save(category);
    }

    public List<Category> findHotCategories(Integer pageNo, Integer pageSize, String sortBy) {
//        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
//
//        Page<Category> pagedResult = categoryRepository.findAll(paging);
//
//        List<Category> list = pagedResult.getContent()
//                .stream()
//                .filter(u -> !u.isDeleteFlag())
//                .collect(Collectors.toList());
//
//        if (pagedResult.hasContent()) {
//            return list;
//        } else {
//            return new ArrayList<>();
//        }
        return findCategoriesPagingAndSorting(pageNo, pageSize, sortBy, OrderBy.DESC);
    }

    public List<Category> findCategoriesPagingAndSorting(Integer pageNo, Integer pageSize, String sortBy, OrderBy order) {
        Pageable paging;
        if (order == OrderBy.DESC) {
            paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        } else {
            paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        }

        Page<Category> pagedResult = categoryRepository.findAll(paging);

        List<Category> list = pagedResult.getContent()
                .stream()
                .filter(u -> !u.isDeleteFlag())
                .collect(Collectors.toList());

        if (pagedResult.hasContent()) {
            return list;
        } else {
            return new ArrayList<>();
        }
    }
}
