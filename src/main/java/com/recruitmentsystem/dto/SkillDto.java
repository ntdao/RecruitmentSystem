package com.recruitmentsystem.dto;

import lombok.Builder;

@Builder
public record SkillDto(
        Integer skillId,
        String skillName,
        String skillNameVI
) {
}
