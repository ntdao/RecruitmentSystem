package com.recruitmentsystem.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record StatisticDto (
        List<StatisticDetailDto> dto,
        Long total
) {
}
