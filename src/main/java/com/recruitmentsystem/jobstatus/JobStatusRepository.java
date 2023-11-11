package com.recruitmentsystem.jobstatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JobStatusRepository extends JpaRepository<JobStatus, Integer> {
    boolean existsJobStatusByJobStatusName(String nameEN);

    boolean existsJobStatusByJobStatusNameVI(String nameVI);

    Optional<JobStatus> findByJobStatusNameVI(String name);
}
