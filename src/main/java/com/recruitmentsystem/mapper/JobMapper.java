package com.recruitmentsystem.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recruitmentsystem.dto.*;
import com.recruitmentsystem.entity.Address;
import com.recruitmentsystem.entity.Job;
import com.recruitmentsystem.entity.Skill;
import com.recruitmentsystem.repository.RecruitmentRepository;
import com.recruitmentsystem.service.*;
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
    private final AddressService addressService;
    private final CategoryService categoryService;
    private final JobTypeService jobTypeService;
    private final SkillService skillService;
    private final RecruitmentRepository recruitmentRepository;

    public JobResponseModel entityToDto(Job job) {
        List<AddressDto> addressList = job.getJobAddresses()
                .stream()
                .map(addressMapper::entityToDto)
                .toList();

        List<SkillDTO> skillList = job.getJobSkills()
                .stream()
                .map(s -> objectMapper.convertValue(s, SkillDTO.class))
                .toList();

        Integer jobCandidate = recruitmentRepository.findAllByStatus(job.getJobId(), List.of(0,1,2)).size();
        Integer jobPass = recruitmentRepository.findAllByStatus(job.getJobId(), List.of(5)).size();
        return JobResponseModel
                .builder()
                .jobId(job.getJobId())
                .jobName(job.getJobName())
                .minEducationLevel(job.getMinEducationLevel())
                .jobExperience(job.getJobExperience())
                .jobGender(job.getJobGender().toString())
                .jobQuantity(job.getJobQuantity())
                .jobCandidate(jobCandidate)
                .jobPass(jobPass)
                .salary(job.getSalary())
                .jobDescription(job.getJobDescription())
                .jobRequirement(job.getJobRequirement())
                .jobBenefit(job.getJobBenefit())
                .companyName(job.getCompany().getCompanyShortName())
                .companyLogo(job.getCompany().getCompanyLogo())
                .jobType(objectMapper.convertValue(job.getJobType(), JobTypeDTO.class))
                .jobStatus(job.getJobStatus())
                .createdAt(job.getCreateDate())
                .jobExpiredDate(job.getJobExpiredDate())
                .category(objectMapper.convertValue(job.getCategory(), CategoryDTO.class))
                .jobSkill(skillList)
                .jobAddress(addressList)
                .build();
    }

    public Job dtoToEntity(JobRequestModel request) {

        Set<Address> address = new HashSet<>();
        for (AddressDto a : request.jobAddresses()) {
            address.add(addressService.addressRequestModelToEntity(a));
        }

        Set<Skill> skill = new HashSet<>();
        for (String s : request.jobSkills()) {
            skill.add(skillService.findById(Integer.parseInt(s)));
        }

        String str = request.jobExpiredDate() + " 00:00";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(str, formatter);

        return Job
                .builder()
                .jobName(request.jobName())
                .jobAddresses(address)
                .salary(request.jobSalary())
                .jobRequirement(request.jobRequirement())
                .jobDescription(request.jobDescription())
                .jobQuantity(Integer.parseInt(request.jobQuantity()))
                .jobGender(request.jobGender())
                .jobType(jobTypeService.findById(Integer.parseInt(request.jobType())))
                .jobExperience(request.jobExperience())
                .category(categoryService.findById(Integer.parseInt(request.categoryId())))
                .jobExpiredDate(dateTime)
                .minEducationLevel(request.minEducationLevel())
                .jobSkills(skill)
                .build();
    }
}
