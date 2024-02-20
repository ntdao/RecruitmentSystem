package com.recruitmentsystem.repository;

import com.recruitmentsystem.entity.Skill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface SkillRepository extends JpaRepository<Skill, Integer> {
    @Query("""
            select r from Skill r where 1 = 1
            and (:code is null or lower(r.code) like %:code%)
            and (:name is null or lower(r.name) like %:name%)
            """)
    Page<Skill> findAll(String code, String name, Pageable pageable);

    @Query(value = "select count(*) from Skill r where 1 = 1 " +
            "and (:id is null or r.id != :id) " +
            "and ((:name is null or lower(r.name) like :name) " +
            "or (:code is null or lower(r.code) like :code))")
    Integer countByNameAndCode(Integer id, String name, String code);

    Optional<Skill> findByName(String name);
}

