package com.recruitmentsystem.category;

import org.springframework.stereotype.Service;

@Service
public class CategoryMapper {
    public CategoryResponseModel categoryToResponseModel(Category category) {
        return (category == null) ? null : CategoryResponseModel
                .builder()
                .id(category.getCategoryId())
                .name(category.getCategoryName())
                .icon(category.getCategoryIcon())
                .total(category.getTotal())
                .build();
    }

    public Category categoryRequestModelToCategory(CategoryRequestModel request) {
        return Category
                .builder()
                .categoryName(request.name())
                .categoryIcon(request.icon())
                .build();
    }
}
