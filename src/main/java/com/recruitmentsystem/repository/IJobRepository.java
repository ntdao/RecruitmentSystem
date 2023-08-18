package com.recruitmentsystem.repository;

import com.recruitmentsystem.entity.Job;
import com.recruitmentsystem.model.job.JobDisplayModel;
import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.NamedNativeQuery;
import jakarta.persistence.SqlResultSetMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IJobRepository extends JpaRepository<Job, Integer>, PagingAndSortingRepository<Job, Integer> {
    @Query(value = "select j.job_id " +
            "from job j " +
            "left join job_category jc on j.job_id = jc.job_id " +
            "left join company_branch b on j.branch_id = b.branch_id " +
            "right join company c on c.company_id = b.company_id " +
            "where c.company_id = ?1 " +
            "group by j.job_id",
            nativeQuery = true)
    List<Integer> findAllJobByCompany(int id);

    @Query(value = "select j.job_id from company_branch b  " +
            "left join job j on b.branch_id = j.branch_id " +
            "where b.branch_id = ?1", nativeQuery = true)
    List<Integer> findAllJobByBranch(int id);

    @Query(value = "select j.job_id from hr_branch h " +
            "left join company_branch b on h.branch_id = b.brand_id " +
            "left join job j on j.branch_id = b.brancd_id " +
            "where h.user_id = ?1", nativeQuery = true)
    List<Integer> findAllJobByHR(int id);

    List<Job> findByJobNameContaining(String jobName);
}
