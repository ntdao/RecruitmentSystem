package com.recruitmentsystem.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recruitmentsystem.dto.InterviewDto;
import com.recruitmentsystem.entity.Interview;
import com.recruitmentsystem.repository.InterviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InterviewService {
    private final InterviewRepository interviewRepository;
    private final ObjectMapper objectMapper;

    public Interview addInterview(InterviewDto dto) {
        Interview interview = objectMapper.convertValue(dto, Interview.class);
        interview.setInterviewStatus(0);
        return interviewRepository.save(interview);
    }

    public void changeStatus(Integer interviewId, Integer status) {
        interviewRepository.changeStatus(interviewId, status);
    }
}
