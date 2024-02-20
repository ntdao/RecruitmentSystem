package com.recruitmentsystem.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InterviewDTO {
        private LocalDateTime interviewTime;
        private Integer interviewType;
        private String interviewAddress;
        private Integer interviewStatus;
}
