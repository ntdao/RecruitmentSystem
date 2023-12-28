package com.recruitmentsystem.repository;

import com.recruitmentsystem.entity.UserWorkingHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserWorkingHistoryRepository extends JpaRepository<UserWorkingHistory, Integer> {
    @Query("""
            select h from UserWorkingHistory h
            join fetch h.user
            where h.user.userId = :userId 
            and h.user.deleteFlag = false
            """)
    List<UserWorkingHistory> findUserWorkingHistory(Integer userId);
}