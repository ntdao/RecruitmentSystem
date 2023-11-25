package com.recruitmentsystem.job;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobRepository extends JpaRepository<Job, Integer> {
    @Query("""
        select j from Job j
        join fetch j.jobSkills
        join fetch j.jobAddresses
        where j.deleteFlag = false
        """)
    List<Job> findAllJobs();
    @Query("""
        select j from Job j
        join fetch j.jobSkills
        join fetch j.jobAddresses
        where j.company.companyId = :id
        and j.deleteFlag = false
        """)
    List<Job> findAllJobByCompany(Integer id);
    @Query("""
        select j from Job j
        join fetch j.jobSkills
        join fetch j.jobAddresses
        where j.deleteFlag = false
        and j.jobName like %?1%
        """)
    List<Job> findByJobName(String jobName);

    Optional<Job> findByJobIdAndDeleteFlagFalse(Integer id);
    @Query(value = """
        select j from Job j
        join fetch j.jobSkills
        join fetch j.jobAddresses
        where j.deleteFlag = false
        """,
    countQuery = """
            select j from Job j
            join fetch j.jobSkills
            join fetch j.jobAddresses
            where j.deleteFlag = false
            """)
    Page<Job> findAllJob(Pageable paging);
}
