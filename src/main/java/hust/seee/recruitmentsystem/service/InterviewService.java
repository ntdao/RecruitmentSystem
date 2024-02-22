package hust.seee.recruitmentsystem.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import hust.seee.recruitmentsystem.dto.InterviewDTO;
import hust.seee.recruitmentsystem.entity.Interview;
import hust.seee.recruitmentsystem.exception.ResourceNotFoundException;
import hust.seee.recruitmentsystem.repository.InterviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InterviewService {
    private final InterviewRepository interviewRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public void changeStatus(Integer interviewId, Integer status) {
        interviewRepository.changeStatus(interviewId, status);
    }

    public InterviewDTO getInterview(Integer applicationId) {
        Interview interview = interviewRepository.findByRecruitment(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Interview not found"));
        return objectMapper.convertValue(interview, InterviewDTO.class);
    }
}
