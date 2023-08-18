package com.recruitmentsystem.repository;

import com.recruitmentsystem.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICompanyRepository extends JpaRepository<Company, Integer>, PagingAndSortingRepository<Company, Integer> {
    boolean existsCompanyByCompanyName(String companyName);
}
