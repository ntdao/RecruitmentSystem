package com.recruitmentsystem.repository;

import com.recruitmentsystem.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ICompanyRepository extends JpaRepository<Company, Integer> {
    boolean existsCompanyByCompanyName(String companyName);
    Optional<Company> findCompanyByCompanyName(String companyName);
}
