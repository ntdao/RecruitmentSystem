package com.recruitmentsystem.repository;

import com.recruitmentsystem.entity.JobLevel;
import com.recruitmentsystem.entity.JobLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IJobLevelRepository extends JpaRepository<JobLevel, Integer> {
    Optional<JobLevel> findById(Integer integer);

    Optional<JobLevel> findByJobLevelName(String roleName);

    boolean existsJobLevelByJobLevelName(String roleName);

}
