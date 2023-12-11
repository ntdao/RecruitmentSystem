package com.recruitmentsystem.industry;

import lombok.Builder;

@Builder
public record IndustryResponseModel (
        Integer industryId,
        String industryName,
        String industryNameVI
){
}
