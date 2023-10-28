package com.recruitmentsystem.repository;

import com.recruitmentsystem.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICompanyRepository extends JpaRepository<Company, Integer>, PagingAndSortingRepository<Company, Integer> {

    boolean existsCompanyByCompanyShortName(String name);

    boolean existsCompanyByCompanyFullName(String name);
    @Query(value = "select c.company_id from company c " +
            "right join company_branch b on c.company_id = b.company_id " +
            "right join hr_branch h on h.branch_id = b.branch_id " +
            "where h.user_id = ?1 " +
            "group by c.company_id", nativeQuery = true)
    Integer findCompanyByHR(int id);
}
