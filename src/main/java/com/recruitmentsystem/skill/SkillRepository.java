package com.recruitmentsystem.skill;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Integer> {
    boolean existsSkillBySkillName(String nameEN);

    boolean existsSkillBySkillNameVI(String nameVI);

    Optional<Skill> findBySkillNameVI(String name);

    Optional<Skill> findBySkillName(String name);
    @Query(value = "select * from skill s " +
            "right join  job_skill js on js.skill_id = s.skill_id " +
            "right join job j on j.job_id = js.job_id " +
            "where j.job_id = :id",
            nativeQuery = true)
    List<Skill> findByJobId(Integer id);
}
