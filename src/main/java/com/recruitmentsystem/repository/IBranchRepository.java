package com.recruitmentsystem.repository;

import com.recruitmentsystem.entity.CompanyBranch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.stream.DoubleStream;

@Repository
public interface IBranchRepository extends JpaRepository<CompanyBranch, Integer> {
    boolean existsBranchByName(String name);

    Optional<CompanyBranch> findBrandByName(String name);

}
