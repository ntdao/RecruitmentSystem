package com.recruitmentsystem.job;

public record JobTopModel(
        String jobName,
        String jobUrl,
        String companyName,
        String companyLogo,
        String salary
) {
}
