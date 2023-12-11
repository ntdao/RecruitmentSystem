package com.recruitmentsystem.job;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recruitmentsystem.address.address.*;
import com.recruitmentsystem.category.CategoryService;
import com.recruitmentsystem.jobposition.JobPositionResponseModel;
import com.recruitmentsystem.jobposition.JobPositionService;
import com.recruitmentsystem.jobtype.JobTypeResponseModel;
import com.recruitmentsystem.jobtype.JobTypeService;
import com.recruitmentsystem.skill.SkillMapper;
import com.recruitmentsystem.skill.SkillResponseModel;
import com.recruitmentsystem.skill.SkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobMapper {
    private final ObjectMapper objectMapper;
    private final AddressMapper addressMapper;
    private final SkillMapper skillMapper;
    private final AddressService addressService;
    private final CategoryService categoryService;
    private final JobPositionService jobPositionService;
    private final JobTypeService jobTypeService;
    private final SalaryService salaryService;
    public JobResponseModel jobToResponseModel(Job job) {
        List<AddressResponseModel> addressList = job.getJobAddresses()
                .stream()
                .map(addressMapper::addressToResponseModel)
                .toList();

        List<SkillResponseModel> skillList = job.getJobSkills()
                .stream()
                .map(skillMapper::skillToResponseModel)
                .toList();

        return JobResponseModel
                .builder()
                .id(job.getJobId())
                .name(job.getJobName())
                .companyName(job.getCompany().getCompanyFullName())
                .companyLogo(job.getCompany().getCompanyLogo())
                .jobAddress(addressList)
                .jobPosition(objectMapper.convertValue(job.getJobPosition(), JobPositionResponseModel.class))
                .salary(job.getSalary())
                .jobUrl(job.getJobUrl())
                .jobRequirement(job.getJobRequirement())
                .jobDescription(job.getJobDescription())
                .jobBenefit(job.getJobBenefit())
                .jobTag(job.getJobTag())
                .jobType(objectMapper.convertValue(job.getJobType(), JobTypeResponseModel.class))
                .jobGender(job.getJobGender().toString())
                .jobQuantity(job.getJobQuantity())
                .jobExperience(job.getJobExperience())
                .jobStatus(job.getJobStatus().toString())
                .category(job.getCategory().getCategoryId())
                .jobSkill(skillList)
                .jobExpiredDate(job.getJobExpiredDate())
                .createdAt(job.getCreateDate())
                .build();
    }

    public Job jobRequestModelToJob(JobRequestModel request) {
        Set<Address> addressList = new HashSet<>();
        for (AddressRequestModel a : request.jobAddresses()) {
            addressList.add(addressService.addressRequestModelToEntity(a));
        }
        return Job
                .builder()
                .jobName(request.name())
                .jobAddresses(addressList)
                .salary(salaryService.salaryRequestToResponse(request.isSalaryVisible(), request.salaryMax(), request.salaryMin()))
                .jobUrl(request.jobUrl())
                .jobRequirement(request.jobRequirement())
                .jobDescription(request.jobDescription())
                .jobBenefit(request.jobBenefit())
                .jobQuantity(request.jobQuantity())
                .jobGender(request.jobGender())
                .jobPosition(jobPositionService.findById(request.jobPosition()))
                .jobType(jobTypeService.findById(request.jobType()))
                .jobExperience(request.jobExperience())
                .category(categoryService.findById(request.categoryId()))
                .jobExpiredDate(request.jobExpiredDate())
                .build();
    }
}
