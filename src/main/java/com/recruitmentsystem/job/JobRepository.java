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
            where j.company.companyId = :id
            and j.deleteFlag = false
            """)
    List<Job> findAllJobByCompany(Integer id);

    @Query(value = """
            select j from Job j
            join fetch j.jobSkills
            join fetch j.jobAddresses
            where j.deleteFlag = false
            and j.jobName like %?1%
            """,
        countQuery = """
            select j from Job j
            join fetch j.jobSkills
            join fetch j.jobAddresses
            where j.deleteFlag = false
            and j.jobName like %?1%
    """)
    Page<Job> findJobByName(String name, Pageable paging);

    @Query("""
            select j from Job j
            join fetch j.jobSkills
            join fetch j.jobAddresses
            where j.deleteFlag = false
            and j.jobId = :id
            """)
    Optional<Job> findById(Integer id);

    @Query(value = "select new com.recruitmentsystem.job.JobTopModel(" +
            "j.jobName, j.jobUrl, j.company.companyShortName, j.company.companyLogo, j.salary)" +
            "from Job j " +
            "where j.deleteFlag = false and j.jobStatus = 'RECRUITING'",
            countQuery = "select new com.recruitmentsystem.job.JobTopModel(" +
                    "j.jobName, j.jobUrl, j.company.companyShortName, j.company.companyLogo, j.salary)" +
                    "from Job j " +
                    "where j.deleteFlag = false and j.jobStatus = 'RECRUITING'")
    Page<JobTopModel> findTopJob(Pageable paging);

    @Query("""
            select j from Job j
            join fetch j.jobSkills
            join fetch j.jobAddresses
            where j.deleteFlag = false
            """)
    List<Job> findAllJob();
    @Query(value = """
            select j from Job j
            join fetch j.jobSkills
            join fetch j.jobAddresses
            where j.deleteFlag = false
            and j.jobName like %?1%
            and j.category.categoryId in ?2
            """,
        countQuery = """
            select j from Job j
            join fetch j.jobSkills
            join fetch j.jobAddresses
            where j.deleteFlag = false
            and j.jobName like %?1%
            and j.category.categoryId in ?2
            """)
    Page<Job> findJobByNameAndCategory(String name, List<Integer> categories, Pageable paging);
}
