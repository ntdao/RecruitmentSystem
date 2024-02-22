package hust.seee.recruitmentsystem.repository;

import hust.seee.recruitmentsystem.entity.CandidateWorkingHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CandidateWorkingHistoryRepository extends JpaRepository<CandidateWorkingHistory, Integer> {
    @Query("""
            select h from CandidateWorkingHistory h
            join fetch h.candidate
            where h.candidate.candidateId = :candidateId 
            and h.candidate.deleteFlag = false
            """)
    List<CandidateWorkingHistory> findCandidateWorkingHistory(Integer candidateId);
}