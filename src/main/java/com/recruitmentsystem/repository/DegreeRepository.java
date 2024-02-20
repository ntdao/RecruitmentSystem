package com.recruitmentsystem.repository;

import com.recruitmentsystem.entity.Degree;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface DegreeRepository extends JpaRepository<Degree, Integer> {
    @Query("""
            select r from Degree r where 1 = 1
            and (:name is null or lower(r.code) like %:name% or lower(r.name) like %:name%)
            """)
    Page<Degree> findAll(String name, Pageable pageable);

    @Query(value = "select count(*) from Degree r where 1 = 1 " +
            "and (:id is null or r.id != :id) " +
            "and ((:name is null or lower(r.name) like :name) " +
            "or (:code is null or lower(r.code) like :code))")
    Integer countByNameAndCode(Integer id, String name, String code);

    Optional<Degree> findByName(String name);
}

