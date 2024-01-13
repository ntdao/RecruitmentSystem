package com.recruitmentsystem.repository;

import com.recruitmentsystem.dto.JobDto;
import com.recruitmentsystem.dto.JobTopModel;
import com.recruitmentsystem.entity.Job;
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
            left join fetch j.jobSkills
            left join fetch j.jobAddresses
            where j.company.companyId = :id
            and j.deleteFlag = false
            """)
    List<Job> findAllJobByCompany(Integer id);

    @Query(value = """
            select j from Job j
            left join fetch j.jobSkills
            left join fetch j.jobAddresses
            where j.deleteFlag = false
            and j.jobName like %?1%
            """,
            countQuery = """
                    select j from Job j
                    left join fetch j.jobSkills
                    left join fetch j.jobAddresses
                    where j.deleteFlag = false
                    and j.jobName like %?1%
                    """)
    Page<Job> findJobByName(String name, Pageable paging);

    @Query("""
            select j from Job j
            left join fetch j.jobSkills
            left join fetch j.jobAddresses
            where j.deleteFlag = false
            and j.jobId = :id
            """)
    Optional<Job> findById(Integer id);

    @Query(value = """
            select new com.recruitmentsystem.dto.JobTopModel(
            j.jobName, j.company.companyShortName, j.company.companyLogo, j.salary)
            from Job j 
            where j.deleteFlag = false and j.jobStatus = 0
            """,
            countQuery = """
                    select new com.recruitmentsystem.dto.JobTopModel(
                    j.jobName, j.company.companyShortName, j.company.companyLogo, j.salary)
                    from Job j 
                    where j.deleteFlag = false and j.jobStatus = 0
                    """)
    Page<JobTopModel> findTopJob(Pageable paging);

    @Query("""
            select j from Job j
            left join fetch j.jobSkills
            left join fetch j.jobAddresses
            where j.deleteFlag = false
            """)
    List<Job> findAllJob();

    @Query(value = """
            select j from Job j
            left join fetch j.jobSkills
            left join fetch j.jobAddresses
            where j.deleteFlag = false
            and j.jobName like %?1%
            and j.category.categoryId in ?2
            """,
            countQuery = """
                    select j from Job j
                    left join fetch j.jobSkills
                    left join fetch j.jobAddresses
                    where j.deleteFlag = false
                    and j.jobName like %?1%
                    and j.category.categoryId in ?2
                    """)
    Page<Job> findJobByNameAndCategory(String name, List<Integer> categories, Pageable paging);

    @Query("""
            select j from Job j
            left join fetch j.jobSkills
            left join fetch j.jobAddresses
            where j.deleteFlag = false
            and j.jobName like %?1%
            """)
    List<Job> findByName(String name);

    @Query("""
            select new com.recruitmentsystem.dto.JobDto(j.jobId, j.jobName)
            from Job j
            where j.jobStatus in :jobStatus
            and j.company.companyId = :companyId
            """)
    List<JobDto> findJobByStatus(List<Integer> jobStatus, Integer companyId);
}
