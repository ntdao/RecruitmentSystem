package hust.seee.recruitmentsystem.service;

import hust.seee.recruitmentsystem.dto.JobDTO;
import hust.seee.recruitmentsystem.entity.Company;
import hust.seee.recruitmentsystem.entity.Job;
import hust.seee.recruitmentsystem.exception.ResourceNotFoundException;
import hust.seee.recruitmentsystem.mapper.JobMapper;
import hust.seee.recruitmentsystem.pagination.PageDto;
import hust.seee.recruitmentsystem.repository.JobRepository;
import hust.seee.recruitmentsystem.utils.DataFormat;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobService {
    private final JobRepository jobRepository;
    private final JobMapper jobMapper;
    private final CompanyService companyService;

    @Transactional
    public void addJob(JobDTO request, Principal principal) {
        Company company = companyService.getCurrentCompany(principal);
        Job job = jobMapper.dtoToEntity(request);
        job.setCompany(company);
        jobRepository.save(job);
    }

    public List<JobDTO> findAllJob() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        Object object = authentication.getPrincipal();
//        if (authentication.getPrincipal().equals("anonymousUser")) {
        return jobRepository.findAllJob()
                .stream()
                .filter(j -> j.getJobStatus() == 0)
                .map(jobMapper::entityToDto)
                .collect(Collectors.toList());
//        }
//        Candidate candidate = candidateService.findCandidateByEmail(authentication.getName());
//        return jobRepository.findAllJobByCategory(candidate.getCategory().getCategoryName())
//                .stream()
//                .filter(j -> j.getJobStatus() == 0)
//                .map(jobMapper::entityToDto)
//                .collect(Collectors.toList());
    }

    public List<JobDTO> findAllJobsByAdmin() {
        return jobRepository.findAllJob()
                .stream()
                .map(jobMapper::entityToDto)
                .collect(Collectors.toList());
    }

    public List<Job> findByCompanyId(Integer companyId) {
        companyService.existsById(companyId);
        return jobRepository.findAllJobByCompany(companyId);
    }

    public List<JobDTO> findJobByCompany(Principal connectedAccount) {
        Company company = companyService.getCurrentCompany(connectedAccount);
        return findByCompanyId(company.getCompanyId())
                .stream()
                .map(jobMapper::entityToDto)
                .toList();
    }

    public List<JobDTO> findJobByCompanyId(Integer companyId) {
        return findByCompanyId(companyId)
                .stream()
                .filter(j -> j.getJobStatus() == 0)
                .map(jobMapper::entityToDto)
                .toList();
    }

    public Job findById(Integer id) {
        return jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job with id " + id + " does not exist"));
    }

    public JobDTO findJobById(Integer id) {
        return jobMapper.entityToDto(findById(id));
    }

    public List<JobDTO> findJobByJobName(String name) {
        return jobRepository.findByName(name)
                .stream()
                .map(jobMapper::entityToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateJob(Integer id, JobDTO requestModel) {
        Job updateJob = findById(id);
        Job oldJob = new Job(id, updateJob, true);
        jobRepository.save(oldJob);

        updateJob = jobMapper.dtoToEntity(requestModel);
        updateJob.setJobId(id);
        jobRepository.save(updateJob);
    }

    public void deleteJob(Integer id) {
        Job job = findById(id);
        job.setDeleteFlag(true);
        jobRepository.save(job);
    }

    public List<JobDTO> getTopJobs(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());

        Page<JobDTO> pagedResult = jobRepository.findTopJob(paging);

        if (pagedResult.hasContent()) {
            return pagedResult.getContent();
        } else {
            return new ArrayList<>();
        }
    }

    public List<JobDTO> getJobByPaging(PageDto pageDto) {
        Pageable paging = PageRequest.of(
                pageDto.getPageNo(),
                pageDto.getPageSize(),
                Sort.Direction.fromString(pageDto.getSortDir()),
                pageDto.getSortBy());

        Page<Job> pagedResult;
        if (pageDto.getKeys().size() == 0) {
            pagedResult = jobRepository.findJobByName(pageDto.getKey(), paging);
        } else {
            pagedResult = jobRepository.findJobByNameAndCategory(pageDto.getKey(), pageDto.getKeys(), paging);
        }
        if (pagedResult.hasContent()) {
            return pagedResult.getContent()
                    .stream()
                    .map(jobMapper::entityToDto)
                    .toList();
        } else {
            return new ArrayList<>();
        }
    }

    public List<Map<String, Object>> findJobByStatus(String jobStatus, Principal principal) {
        Company company = companyService.getCurrentCompany(principal);
        List<Integer> jobStatusList;
        if (jobStatus.equals("")) {
            jobStatusList = Arrays.asList(0, 1, 2);
        } else {
            jobStatusList = Collections.singletonList(Integer.parseInt(jobStatus));
        }
        return jobRepository.findJobByStatus(jobStatusList, company.getCompanyId());
    }

//    public StatisticDTO getQuantity() {
//        List<Map<String, Object>> map = jobRepository.getQuantity();
//        return Utils.getStatistic(map);
//    }

    public Page<JobDTO> getJobPaging(JobDTO dto) {
        Pageable pageable = PageRequest.of(dto.getPage() - 1, dto.getSize());
        return jobRepository.findAll(
                        DataFormat.lower(dto.getJobName()),
                        dto.getCategoryId(),
                        dto.getJobTypeId(),
                        dto.getProvinceCode(),
                        pageable)
                .map(jobMapper::entityToDto);
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void changeJobStatus() {
        System.out.println("Start Change Job Status at " + new Date());

        List<Job> jobs = jobRepository.findAllJob().stream().filter(j -> j.getJobStatus() == 0).toList();

        jobs.forEach(x -> {
            LocalDate date = x.getJobExpiredDate().toLocalDate();
            LocalDate now = LocalDate.now();
            if (date.isBefore(now)) {
                x.setJobStatus(2);
                jobRepository.save(x);
            }
        });

        System.out.println("End Change Job Status at " + new Date());
    }
}
