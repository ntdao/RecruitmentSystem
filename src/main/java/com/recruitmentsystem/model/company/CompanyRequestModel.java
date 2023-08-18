package com.recruitmentsystem.model.company;

public record CompanyRequestModel(
        String companyName,
        String companyAddress,
        String companyLogo,
        String companySlogan,
        String companyIntroduction,
        String companyImage,
        String companyUrl,
        String companyField
) {
}
