package com.recruitmentsystem.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Builder
public record CandidateDto (
        Integer id,
        Integer accountId,
        String email,
        String fullName,
        String phoneNumber,
        AddressDto address,
        String gender,
        LocalDate birthday,
        String imgUrl,
        LocalDateTime createDate,
        LocalDateTime lastModified,
        String roleName,
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        List<CandidateEducationDto> education,
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        List<CandidateWorkingHistoryDto> workingHistory,
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        List<SkillDTO> skill,
        String desiredJob,
        String educationLevel,
        CategoryDTO category,
        String cvUrl,
        @JsonIgnore
        String password,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        String currentPass,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        String newPass,

        String key,
        Integer page,
        Integer size
) {
}
