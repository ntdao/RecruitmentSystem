package com.recruitmentsystem.usereducation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserEducationRepository extends JpaRepository<UserEducation, Integer> {
    @Query("""
            select ue from UserEducation ue
            join fetch ue.degree
            join fetch ue.user
            where ue.user.userId = :id
            and ue.user.deleteFlag = false 
            """)
    List<UserEducation> findUserEducationByUser(Integer id);
}