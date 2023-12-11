//package com.recruitmentsystem.industry;
//
//import org.springframework.stereotype.Service;
//
//@Service
//public class IndustryMapper {
//    public IndustryResponseModel industryToResponseModel(Industry industry) {
//        return IndustryResponseModel
//                .builder()
//                .industryId(industry.getIndustryId())
//                .industryName(industry.getIndustryName())
//                .industryNameVI(industry.getIndustryNameVI())
//                .build();
//    }
//
//    public Industry industryRequestModelToIndustry(IndustryRequestModel request) {
//        return Industry
//                .builder()
//                .industryName(request.industryName())
//                .industryNameVI(request.industryNameVI())
//                .build();
//    }
//}
