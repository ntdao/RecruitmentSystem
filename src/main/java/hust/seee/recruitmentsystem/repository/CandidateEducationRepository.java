package hust.seee.recruitmentsystem.repository;

import hust.seee.recruitmentsystem.entity.CandidateEducation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CandidateEducationRepository extends JpaRepository<CandidateEducation, Integer> {
    @Query("""
            select ue from CandidateEducation ue
            join fetch ue.degree
            join fetch ue.candidate
            where ue.candidate.candidateId = :id
            and ue.candidate.deleteFlag = false 
            """)
    List<CandidateEducation> findCandidateEducationByCandidate(Integer id);
}