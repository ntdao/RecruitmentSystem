package com.recruitmentsystem.dto;

import lombok.Builder;

@Builder
public record CategoryDto(
        Integer categoryId,
        String categoryName,
        String categoryIcon,
        Integer categoryTotal
){
}
