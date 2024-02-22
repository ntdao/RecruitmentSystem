package hust.seee.recruitmentsystem.repository;

import hust.seee.recruitmentsystem.entity.Interview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InterviewRepository extends JpaRepository<Interview, Integer> {
    @Modifying
    @Query("""
                 update Interview i 
                 set i.interviewStatus = :status
                 where i.interviewId = :interviewId
            """
    )
    void changeStatus(Integer interviewId, Integer status);

    Optional<Interview> findByRecruitment(Integer applicationId);
}
