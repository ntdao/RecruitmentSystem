package com.recruitmentsystem.skill;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Integer> {
    boolean existsSkillBySkillName(String nameEN);

    boolean existsSkillBySkillNameVI(String nameVI);

    Optional<Skill> findBySkillNameVI(String name);

    Optional<Skill> findBySkillName(String name);
}
