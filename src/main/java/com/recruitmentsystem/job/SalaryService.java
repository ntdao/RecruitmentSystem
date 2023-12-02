package com.recruitmentsystem.job;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.stereotype.Service;

@Service
public class SalaryService {
    public String salaryRequestToResponse(boolean isSalaryVisible, Integer salaryMax, Integer salaryMin) {
        String prettySalary = salaryMin + " - " + salaryMax + " đồng";
        if (!isSalaryVisible) {
            prettySalary = "Thỏa thuận";
        }
        if (salaryMin == null) {
            prettySalary = "Up to " + salaryMax + " đồng";
        }
        if (salaryMax == null) {
            prettySalary = "Từ " + salaryMin + " đồng";
        }
        return prettySalary;
    }
}
