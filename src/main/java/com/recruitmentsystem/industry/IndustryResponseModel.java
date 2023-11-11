package com.recruitmentsystem.industry;

import lombok.Builder;

@Builder
public record IndustryResponseModel (
        Integer id,
        String nameEN,
        String nameVI
){
}
