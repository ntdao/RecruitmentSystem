package hust.seee.recruitmentsystem.controller;

import hust.seee.recruitmentsystem.dto.JobDTO;
import hust.seee.recruitmentsystem.enums.Gender;
import hust.seee.recruitmentsystem.response.BaseResponse;
import hust.seee.recruitmentsystem.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class JobController {
    private final JobService jobService;

    @GetMapping("/jobs/all")
    public List<JobDTO> getAllJobs() {
        return jobService.findAllJob();
    }

    @GetMapping("/jobs/top")
    public List<JobDTO> getTopJobs(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "6") Integer pageSize,
            @RequestParam(defaultValue = "salaryMax") String sortBy) {
        return jobService.getTopJobs(pageNo, pageSize, sortBy);
    }

    @PostMapping("/jobs/get-all")
    public ResponseEntity<BaseResponse> getJobPaging(@RequestBody JobDTO request) {
        BaseResponse baseResponse = new BaseResponse();
        Page<JobDTO> page = jobService.getJobPaging(request);
        baseResponse.setAdditionalProperty("data", page.getContent());
        baseResponse.setAdditionalProperty("total", page.getTotalElements());

        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    @GetMapping("/jobs/find")
    public List<JobDTO> getJobByName(@RequestParam("name") String name) {
        return jobService.findJobByJobName(name);
    }

    @GetMapping("/job/{jobId}")
    public JobDTO getJobById(@PathVariable("jobId") Integer jobId) {
        return jobService.findJobById(jobId);
    }

    @GetMapping("/jobs/job-gender")
    public ResponseEntity<?> getAllJobGender() {
        return ResponseEntity.ok(Gender.getAll());
    }

    @PostMapping("/jobs/{companyId}/all")
    public ResponseEntity<BaseResponse> getAllJob(@PathVariable("companyId") Integer id, @RequestBody JobDTO dto) {
        BaseResponse baseResponse = new BaseResponse();
        Page<JobDTO> page = jobService.findJobByCompanyId(id, dto);
        baseResponse.setAdditionalProperty("data", page.getContent());
        baseResponse.setAdditionalProperty("total", page.getTotalElements());

        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    @GetMapping("/admin/manage/jobs/all")
    public ResponseEntity<BaseResponse> adminGetAllJob(@RequestBody JobDTO dto) {
        BaseResponse baseResponse = new BaseResponse();
        Page<JobDTO> page = jobService.findAllJobsByAdmin(dto);
        baseResponse.setAdditionalProperty("data", page.getContent());
        baseResponse.setAdditionalProperty("total", page.getTotalElements());

        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    @GetMapping("/manage/jobs/find/{jobId}")
    public JobDTO adminGetJobById(@PathVariable("jobId") Integer id) {
        return jobService.findJobById(id);
    }

    @DeleteMapping("/manage/jobs/delete/{jobId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void adminDeleteJob(@PathVariable("jobId") Integer id) {
        jobService.deleteJob(id);
    }

    @GetMapping("/company/manage/jobs/all")
    public ResponseEntity<BaseResponse> getCompanyJob(@RequestBody JobDTO dto, Principal connectedUser) {
        BaseResponse baseResponse = new BaseResponse();
        Page<JobDTO> page = jobService.findJobByCompany(dto, connectedUser);
        baseResponse.setAdditionalProperty("data", page.getContent());
        baseResponse.setAdditionalProperty("total", page.getTotalElements());

        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    @PostMapping("/company/manage/jobs/add")
    public void addJob(@RequestBody JobDTO jobRequestModel,
                       Principal principal) {
        jobService.addJob(jobRequestModel, principal);
    }

    @PostMapping("/company/manage/jobs/update/{jobId}")
    public void updateJob(@PathVariable("jobId") Integer jobId,
                          @RequestBody JobDTO jobRequestModel) {
        jobService.updateJob(jobId, jobRequestModel);
    }

    @GetMapping("/company/manage/jobs")
    public List<Map<String, Object>> findJobByStatus(@RequestParam("jobStatus") String jobStatus, Principal principal) {
        return jobService.findJobByStatus(jobStatus, principal);
    }
}
