package com.recruitmentsystem.job;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recruitmentsystem.address.address.*;
import com.recruitmentsystem.category.CategoryService;
//import com.recruitmentsystem.jobposition.JobPositionService;
import com.recruitmentsystem.jobtype.JobTypeResponseModel;
import com.recruitmentsystem.jobtype.JobTypeService;
import com.recruitmentsystem.skill.SkillMapper;
import com.recruitmentsystem.skill.SkillResponseModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class JobMapper {
    private final ObjectMapper objectMapper;
    private final AddressMapper addressMapper;
    private final SkillMapper skillMapper;
    private final AddressService addressService;
    private final CategoryService categoryService;
//    private final JobPositionService jobPositionService;
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
                .salary(job.getSalary())
                .jobUrl(job.getJobUrl())
                .jobRequirement(job.getJobRequirement())
                .jobDescription(job.getJobDescription())
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

//        Set<Address> addressList = new HashSet<>();
//        for (AddressRequestModel a : request.jobAddresses()) {
//            addressList.add(addressService.addressRequestModelToEntity(a));
//        }

        String str = request.jobExpiredDate() + " 00:00";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(str, formatter);

        return Job
                .builder()
                .jobName(request.name())
//                .jobAddresses(addressList)
                .salary(request.salary())
                .salaryMin(Integer.parseInt(request.salaryMin()))
                .salaryMax(Integer.parseInt(request.salaryMax()))
                .jobUrl(request.jobUrl())
                .jobRequirement(request.jobRequirement())
                .jobDescription(request.jobDescription())
                .jobQuantity(Integer.parseInt(request.jobQuantity()))
                .jobGender(request.jobGender())
                .jobType(jobTypeService.findById(Integer.parseInt(request.jobType())))
                .jobExperience(request.jobExperience())
                .category(categoryService.findById(Integer.parseInt(request.categoryId())))
                .jobExpiredDate(dateTime)
                .minEducationLevel(request.minEducationLevel())
                .build();
    }
}
