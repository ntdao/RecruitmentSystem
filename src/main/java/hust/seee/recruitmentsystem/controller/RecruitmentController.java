package hust.seee.recruitmentsystem.controller;

import hust.seee.recruitmentsystem.dto.InterviewDTO;
import hust.seee.recruitmentsystem.dto.RecruitmentDTO;
import hust.seee.recruitmentsystem.service.InterviewService;
import hust.seee.recruitmentsystem.service.RecruitmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/recruitment")
@RequiredArgsConstructor
public class RecruitmentController {
    private final InterviewService interviewService;
    private final RecruitmentService recruitmentService;

    @GetMapping("/all")
    public List<RecruitmentDTO> getAll() {
        return recruitmentService.findAll();
    }

    @PostMapping("/apply-job/{jobId}")
    public void candidateApplyJob(@PathVariable("jobId") Integer jobId, Principal principal) {
        recruitmentService.candidateApplyJob(principal, jobId);
    }

    @PostMapping("/check-apply/{jobId}")
    public boolean checkApplyJob(@PathVariable("jobId") Integer jobId, Principal principal) {
        return recruitmentService.checkApplyJob(principal, jobId);
    }

    @GetMapping("/candidate/all")
    public List<RecruitmentDTO> getAllApplicationByCandidateId(Principal principal) {
        return recruitmentService.getAllByCandidateId(principal);
    }

    @GetMapping("/{applicationId}")
    public RecruitmentDTO getByApplicationId(@PathVariable("applicationId") Integer applicationId) {
        return recruitmentService.getDtoByApplicationId(applicationId);
    }

    @GetMapping("/manage/candidate")
    public List<RecruitmentDTO> getAllCandidateByJob(@RequestParam String jobId,
                                                     @RequestParam String statusId) {
        return recruitmentService.getAllCandidateByJob(jobId, statusId);
    }

    @GetMapping("/manage/interviewee")
    public List<RecruitmentDTO> getAllIntervieweeByJob(@RequestParam String jobId,
                                                       @RequestParam String statusId) {
        return recruitmentService.getAllIntervieweeByJob(jobId, statusId);
    }

    @PostMapping("/manage/change-status")
    public void changeStatus(@RequestParam("applicationId") Integer applicationId,
                             @RequestParam("status") Integer status) {
        recruitmentService.changeStatus(applicationId, status);
    }

    @PostMapping("/manage/add-interview/{applicationId}")
    public void addInterview(@PathVariable("applicationId") Integer applicationId,
                             @RequestBody InterviewDTO dto) {
        recruitmentService.addInterview(applicationId, dto);
    }

    @PostMapping("/get-interview/{applicationId}")
    public InterviewDTO getInterview(@PathVariable("applicationId") Integer applicationId) {
        return interviewService.getInterview(applicationId);
    }

    @PostMapping("/change-interview-status")
    public void changeInterviewStatus(@RequestParam("interviewId") Integer interviewId,
                                      @RequestParam("status") Integer status) {
        interviewService.changeStatus(interviewId, status);
    }
}
