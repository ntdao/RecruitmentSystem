package com.recruitmentsystem.jobstatus;

import lombok.Builder;

@Builder
public record JobStatusResponseModel(
        Integer id,
        String nameEN,
        String nameVI
) {
}
