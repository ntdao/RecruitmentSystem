package hust.seee.recruitmentsystem.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import hust.seee.recruitmentsystem.dto.CategoryDTO;
import hust.seee.recruitmentsystem.dto.JobDTO;
import hust.seee.recruitmentsystem.dto.JobTypeDTO;
import hust.seee.recruitmentsystem.dto.SkillDTO;
import hust.seee.recruitmentsystem.entity.Job;
import hust.seee.recruitmentsystem.entity.Skill;
import hust.seee.recruitmentsystem.repository.RecruitmentRepository;
import hust.seee.recruitmentsystem.service.CategoryService;
import hust.seee.recruitmentsystem.service.JobTypeService;
import hust.seee.recruitmentsystem.service.SkillService;
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
    private final CategoryService categoryService;
    private final JobTypeService jobTypeService;
    private final SkillService skillService;
    private final RecruitmentRepository recruitmentRepository;
    private final AddressMapper addressMapper;

    public JobDTO entityToDto(Job job) {
        List<SkillDTO> skillList = job.getJobSkills()
                .stream()
                .map(s -> objectMapper.convertValue(s, SkillDTO.class))
                .toList();

        Integer jobCandidate = recruitmentRepository.findAllByStatus(job.getJobId(), List.of(0, 1, 2)).size();
        Integer jobPass = recruitmentRepository.findAllByStatus(job.getJobId(), List.of(5)).size();
        return JobDTO
                .builder()
                .jobId(job.getJobId())
                .jobName(job.getJobName())
                .minEducationLevel(job.getMinEducationLevel())
                .jobExperience(job.getJobExperience())
                .jobGender(job.getJobGender())
                .jobQuantity(job.getJobQuantity())
                .jobCandidate(jobCandidate)
                .jobPass(jobPass)
                .jobSalary(job.getSalary())
                .jobDescription(job.getJobDescription())
                .jobRequirement(job.getJobRequirement())
                .jobBenefit(job.getJobBenefit())
                .companyName(job.getCompany().getCompanyShortName())
                .companyLogo(job.getCompany().getCompanyLogo())
                .jobType(objectMapper.convertValue(job.getJobType(), JobTypeDTO.class))
                .jobStatus(job.getJobStatus())
                .createDate(job.getCreateDate())
                .jobExpiredDate(job.getJobExpiredDate())
                .category(objectMapper.convertValue(job.getCategory(), CategoryDTO.class))
                .jobSkill(skillList)
                .jobAddress(addressMapper.entityToDto(job.getJobAddress()))
                .build();
    }

    public Job dtoToEntity(JobDTO request) {
        Set<Skill> skill = new HashSet<>();
        for (String s : request.getJobSkills()) {
            skill.add(skillService.findById(Integer.parseInt(s)));
        }

//        String str = request.getJobExpiredDate() + " 00:00";
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
//        LocalDateTime dateTime = LocalDateTime.parse(str, formatter);

        return Job
                .builder()
                .jobName(request.getJobName())
                .jobAddress(addressMapper.dtoToEntity(request.getJobAddress()))
                .salary(request.getJobSalary())
                .jobRequirement(request.getJobRequirement())
                .jobDescription(request.getJobDescription())
                .jobQuantity(request.getJobQuantity())
                .jobGender(request.getJobGender())
                .jobType(jobTypeService.findById(request.getJobTypeId()))
                .jobExperience(request.getJobExperience())
                .category(categoryService.findById(request.getCategoryId()))
                .jobExpiredDate(request.getJobExpiredDate())
                .minEducationLevel(request.getMinEducationLevel())
                .jobSkills(skill)
                .build();
    }
}
