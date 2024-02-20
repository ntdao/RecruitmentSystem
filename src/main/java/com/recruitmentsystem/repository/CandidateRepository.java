package com.recruitmentsystem.repository;

import com.recruitmentsystem.entity.Candidate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Integer> {
    @Query("""
            select u from Candidate u
            left join fetch u.candidateEducations
            left join fetch u.workingHistories
            left join fetch u.candidateSkills
            join fetch u.account
            where u.account.deleteFlag = false
            and u.deleteFlag = false
            """)
    List<Candidate> findAllCandidate();

    @Query(value = """
            select u from Candidate u 
            left join fetch u.candidateEducations 
            left join fetch u.workingHistories
            left join fetch u.candidateSkills
            join fetch u.account
            where u.account.deleteFlag = false
            and (:key is null or lower(u.fullName) like %:key%
             or lower(u.account.email) like %:key%)
            """,
        countQuery = """
                select u from Candidate u 
                left join fetch u.candidateEducations 
                left join fetch u.workingHistories
                left join fetch u.candidateSkills
                join fetch u.account
                where u.account.deleteFlag = false
                and (:key is null or lower(u.fullName) like %:key%
                 or lower(u.account.email) like %:key%)
                """)
    Page<Candidate> findAllCandidate(String key, Pageable paging);

    @Query("""
            select u from Candidate u
            left join fetch u.candidateEducations
            left join fetch u.workingHistories
            left join fetch u.candidateSkills
            join fetch u.account
            where u.deleteFlag = false
            and u.account.deleteFlag = false
            and u.account.email = :email
            """)
    Optional<Candidate> findCandidateByEmail(String email);

    @Query("""
            select u from Candidate u 
            left join fetch u.candidateEducations
            left join fetch u.workingHistories 
            left join fetch u.candidateSkills
            join fetch u.account
            where (:key is null or lower(u.fullName) like %:key%
             or lower(u.account.email) like %:key%)
            and u.deleteFlag = false
            """)
    List<Candidate> findAllCandidateByKey(String key);

    @Query("""
            select u from Candidate u
            left join fetch u.candidateEducations
            left join fetch u.workingHistories
            left join fetch u.candidateSkills
            join fetch u.account
            where u.deleteFlag = false
            and u.account.deleteFlag = false
            and u.candidateId = :id
            """)
    Optional<Candidate> findCandidateById(Integer id);

    @Query("""
            select u from Candidate u
            left join fetch u.candidateEducations
            left join fetch u.workingHistories
            left join fetch u.candidateSkills
            join fetch u.account
            where u.deleteFlag = false
            and u.account.deleteFlag = false
            and u.candidateId in :ids
            """)
    List<Candidate> findListCandidateByIds(List<Integer> ids);

    //    @Query("""
//            select new com.recruitmentsystem.dto.StatisticDetailDto(month(c.createDate), year(c.createDate), count(*))
//            from Candidate c where c.deleteFlag = false
//            group by month(c.createDate), year(c.createDate)
//            """)
    @Query("""
            select month(c.createDate) as month, year(c.createDate) as year, count(*) as quantity
            from Candidate c where c.deleteFlag = false 
            group by month(c.createDate), year(c.createDate)
            """)
    List<Map<String, Object>> getQuantity();
}
