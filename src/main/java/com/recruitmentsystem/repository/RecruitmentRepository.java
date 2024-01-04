package com.recruitmentsystem.repository;

import com.recruitmentsystem.entity.Recruitment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecruitmentRepository extends JpaRepository<Recruitment, Integer> {
    @Query("""
            select distinct a.user.userId from candidate_apply_job a
            where a.job.jobId = :jobId
            and a.applicationStatus in :status
            """)
    List<Integer> findAllByStatus(Integer jobId, List<Integer> status);

    @Modifying
    @Query("""
            update candidate_apply_job a 
            set a.applicationId = :status
            where a.applicationId = :applicationId
            """)
    void changeStatus(Integer applicationId, Integer status);

    @Query("""
            select distinct a.user.userId from candidate_apply_job a
            join a.job j
            where j.company.companyId = :companyId
            """)
    List<Integer> findAllCandidatesByCompany(Integer companyId);
}