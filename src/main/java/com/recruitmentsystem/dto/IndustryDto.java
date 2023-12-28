package com.recruitmentsystem.dto;

import lombok.Builder;

@Builder
public record IndustryDto(
        Integer industryId,
        String industryName,
        String industryNameVI
){
}
