package com.recruitmentsystem.jobtype;

import lombok.Builder;

@Builder
public record JobTypeResponseModel(
        Integer id,
        String nameEN,
        String nameVI
) {
}
