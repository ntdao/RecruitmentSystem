package com.recruitmentsystem.industry;

import org.springframework.stereotype.Service;

@Service
public class IndustryMapper {
    public IndustryResponseModel industryToResponseModel(Industry industry) {
        return IndustryResponseModel
                .builder()
                .id(industry.getIndustryId())
                .nameEN(industry.getIndustryName())
                .nameVI(industry.getIndustryNameVI())
                .build();
    }

    public Industry industryRequestModelToIndustry(IndustryRequestModel request) {
        return Industry
                .builder()
                .industryName(request.nameEN())
                .industryNameVI(request.nameVI())
                .build();
    }
}
