package com.recruitmentsystem.jobtype;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JobTypeRepository extends JpaRepository<JobType, Integer> {
    boolean existsJobTypeByJobTypeName(String nameEN);

    boolean existsJobTypeByJobTypeNameVI(String nameVI);

    Optional<JobType> findByJobTypeNameVI(String name);
}
