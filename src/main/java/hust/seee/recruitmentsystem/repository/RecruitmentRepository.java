package hust.seee.recruitmentsystem.repository;

import hust.seee.recruitmentsystem.entity.Recruitment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecruitmentRepository extends JpaRepository<Recruitment, Integer> {
    @Query("""
            select a.applicationId from candidate_apply_job a
            where a.job.jobId = :jobId
            and a.applicationStatus in :status
            """)
    List<Integer> findAllByStatus(Integer jobId, List<Integer> status);

    @Modifying
    @Query("""
            update candidate_apply_job a 
            set a.applicationStatus = :status
            where a.applicationId = :applicationId
            """)
    void changeStatus(Integer applicationId, Integer status);

    @Query("""
            select distinct a.candidate.candidateId from candidate_apply_job a
            join a.job j
            where j.company.companyId = :companyId
            """)
    List<Integer> findAllCandidatesByCompany(Integer companyId);

    @Query("""
            select caj from candidate_apply_job caj
            join fetch caj.job
            join fetch caj.candidate u
            where caj.candidate.candidateId = :candidateId
            """)
    List<Recruitment> findAllByCandidateId(Integer candidateId);

    @Query("""
            select exists (select a.applicationId 
            from candidate_apply_job a
            where a.candidate.candidateId = :userId  
            and a.job.jobId = :jobId)
            """)
    boolean isApplied(Integer userId, Integer jobId);

    @Query("""
            select caj from candidate_apply_job caj
            join fetch caj.job
            join fetch caj.candidate u
            where caj.applicationId = :applicationId
            """)
    Optional<Recruitment> findByApplicationId(Integer applicationId);

    @Query("""
            select caj from candidate_apply_job caj
            join fetch caj.job
            join fetch caj.candidate u
            where caj.applicationId in :ids
            """)
    List<Recruitment> findAllByIds(List<Integer> ids);

    @Query("select count(*) from candidate_apply_job where 1 = 1 and job.jobId = :jobId")
    Integer countByJob(Integer jobId);

    @Query("""
                select r from candidate_apply_job r 
                left join fetch r.candidate
                left join fetch r.job
            """)
    List<Recruitment> findAll();
}