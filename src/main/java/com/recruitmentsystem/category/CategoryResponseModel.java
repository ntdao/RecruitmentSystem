package com.recruitmentsystem.category;

import lombok.Builder;

@Builder
public record CategoryResponseModel(
        Integer id,
        String name,
        String icon,
        Integer total
){
}
