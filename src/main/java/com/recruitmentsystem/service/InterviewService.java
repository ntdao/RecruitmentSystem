package com.recruitmentsystem.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recruitmentsystem.dto.InterviewDTO;
import com.recruitmentsystem.entity.Interview;
import com.recruitmentsystem.exception.ResourceNotFoundException;
import com.recruitmentsystem.repository.InterviewRepository;
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
