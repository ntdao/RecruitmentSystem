package com.recruitmentsystem.repository;

import com.recruitmentsystem.entity.JobType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface JobTypeRepository extends JpaRepository<JobType, Integer> {
    @Query("""
            select r from JobType r where 1 = 1
            and (:name is null or lower(r.code) like %:name% or lower(r.name) like %:name%)
            """)
    Page<JobType> findAll(String name, Pageable pageable);

    @Query(value = "select count(*) from JobType r where 1 = 1 " +
            "and (:id is null or r.id != :id) " +
            "and ((:name is null or lower(r.name) like :name) " +
            "or (lower(r.code) like :code))")
    Integer countByNameAndCode(Integer id, String name, String code);

    Optional<JobType> findByName(String name);
}

