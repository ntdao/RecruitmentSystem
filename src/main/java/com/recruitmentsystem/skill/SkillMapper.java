package com.recruitmentsystem.skill;

import org.springframework.stereotype.Service;

@Service
public class SkillMapper {
    public SkillResponseModel skillToResponseModel(Skill skill) {
        return SkillResponseModel
                .builder()
                .id(skill.getSkillId())
                .nameEN(skill.getSkillName())
                .nameVI(skill.getSkillNameVI())
                .build();
    }

    public Skill skillRequestModelToSkill(SkillRequestModel request) {
        return Skill
                .builder()
                .skillName(request.nameEN())
                .skillNameVI(request.nameVI())
                .build();
    }
}
