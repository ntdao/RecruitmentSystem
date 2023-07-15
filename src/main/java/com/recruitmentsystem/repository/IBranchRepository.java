package com.recruitmentsystem.repository;

import com.recruitmentsystem.entity.CompanyBranch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IBranchRepository extends JpaRepository<CompanyBranch, Integer> {
    boolean existsByBranchName(String name);

    Optional<CompanyBranch> findByBranchName(String name);

}
