package com.recruitmentsystem.service;

import com.recruitmentsystem.common.exception.ResourceAlreadyExistsException;
import com.recruitmentsystem.common.exception.ResourceNotFoundException;
import com.recruitmentsystem.entity.Category;
import com.recruitmentsystem.repository.ICategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final ICategoryRepository categoryRepository;
    
    public void addCategory(Category request) {
        String name = request.getCategoryName();
        if (categoryRepository.existsCategoryByCategoryName(name)) {
            throw new ResourceAlreadyExistsException("username already taken");
        }

        request.setCreatedAt(Instant.now());
//        user.setCreatedBy(getCategoryByToken(request.token()).getId());
        categoryRepository.save(request);
    }

    public List<Category> findAllCategories() {
        List<Category> users = categoryRepository.findAll();
        return users.stream()
                .filter(user -> !user.isDeleteFlag())
                .collect(Collectors.toList());
    }

    public Category findById(Integer id) {
        return categoryRepository.findById(id)
                .filter(user -> !user.isDeleteFlag())
                .orElseThrow(() -> new ResourceNotFoundException("Category with id " + id + " does not exist"));
    }

//    public Category findCategoryByCategoryname(String username) {
//        return categoryRepository.findCategoryByCategoryname(username)
//                .filter(user -> !user.isDeleteFlag())
//                .orElseThrow(() -> new ResourceNotFoundException("Category with username " + username + " does not exist"));
//    }
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

        // update user
        updateCategory.setCategoryId(id);
        updateCategory.setCreatedAt(oldCategory.getCreatedAt());
        updateCategory.setCreatedBy(oldCategory.getCreatedBy());
        updateCategory.setUpdatedAt(oldCategory.getUpdatedAt());
//        updateCategory.setUpdatedBy(getCategoryByToken(request.token()).getId());
        categoryRepository.save(updateCategory);
    }

    public void deleteCategory(Integer id) {
        Category user;
        try {
            user = findById(id);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Category with id " + id + " does not exist");
        }
        user.setDeleteFlag(true);
        categoryRepository.save(user);
    }
}
