package com.recruitmentsystem.jobposition;

import lombok.Builder;

@Builder
public record JobPositionResponseModel(
        Integer id,
        String nameEN,
        String nameVI
) {
}
