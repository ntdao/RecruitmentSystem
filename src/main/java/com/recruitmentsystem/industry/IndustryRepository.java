package com.recruitmentsystem.industry;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IndustryRepository extends JpaRepository<Industry, Integer> {
    Optional<Industry> findByIndustryNameVI(String industryName);

    boolean existsIndustryByIndustryName(String name);

    boolean existsIndustryByIndustryNameVI(String name);

    List<Industry> findByIndustryNameVIContains(String name);
}
