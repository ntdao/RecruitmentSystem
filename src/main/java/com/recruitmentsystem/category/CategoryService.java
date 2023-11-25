package com.recruitmentsystem.category;

import com.recruitmentsystem.common.exception.ResourceAlreadyExistsException;
import com.recruitmentsystem.common.exception.ResourceNotFoundException;
import com.recruitmentsystem.common.myEnum.OrderBy;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryMapper categoryMapper;
    private final CategoryRepository categoryRepository;

    private void checkDuplicatedCategoryName(String name) {
        if (categoryRepository.existsCategoryByCategoryName(name)) {
            throw new ResourceAlreadyExistsException("Category name already taken");
        }
    }

    public void addCategory(CategoryRequestModel request) {
        checkDuplicatedCategoryName(request.name());
        Category category = categoryMapper.categoryRequestModelToCategory(request);
        categoryRepository.save(category);
    }

    public List<CategoryResponseModel> findAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::categoryToResponseModel)
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

    public CategoryResponseModel findCategoryResponseModelById(Integer id) {
        return categoryMapper.categoryToResponseModel(findById(id));
    }

    @Transactional
    public void updateCategory(Integer id, CategoryRequestModel request) {
        Category updateCategory = findById(id);
        Category categoryRequest = categoryMapper.categoryRequestModelToCategory(request);
        categoryRequest.setCategoryId(id);
        categoryRepository.save(updateCategory);
    }

    public void deleteCategory(Integer id) {
        Category category = findById(id);
        categoryRepository.delete(category);
    }

    public List<CategoryResponseModel> findHotCategories(Integer pageNo, Integer pageSize, String sortBy) {
        return findCategoriesPagingAndSorting(pageNo, pageSize, sortBy, OrderBy.DESC);
    }

    public List<CategoryResponseModel> findCategoriesPagingAndSorting(Integer pageNo, Integer pageSize, String sortBy, OrderBy order) {
        Pageable paging;
        if (order == OrderBy.DESC) {
            paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        } else {
            paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        }

        Page<Category> pagedResult = categoryRepository.findAll(paging);

        List<CategoryResponseModel> list = pagedResult.getContent()
                .stream()
                .map(categoryMapper::categoryToResponseModel)
                .collect(Collectors.toList());

        if (pagedResult.hasContent()) {
            return list;
        } else {
            return new ArrayList<>();
        }
    }
}
