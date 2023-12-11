package com.recruitmentsystem.jobtype;

import lombok.Builder;

@Builder
public record JobTypeResponseModel(
        Integer jobTypeId,
        String jobTypeName,
        String jobTypeNameVI
) {
}
