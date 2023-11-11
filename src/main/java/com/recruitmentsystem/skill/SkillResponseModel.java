package com.recruitmentsystem.skill;

import lombok.Builder;

@Builder
public record SkillResponseModel(
        Integer id,
        String nameEN,
        String nameVI
) {
}
