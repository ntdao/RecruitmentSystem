package com.recruitmentsystem.repository;

import com.recruitmentsystem.entity.Industry;
import com.recruitmentsystem.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IIndustryRepository extends JpaRepository<Industry, Integer> {
    Optional<Industry> findByIndustryNameVn(String industryName);
}
