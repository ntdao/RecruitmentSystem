package com.recruitmentsystem.company;

import com.recruitmentsystem.company.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer>, PagingAndSortingRepository<Company, Integer> {

    boolean existsCompanyByCompanyShortName(String name);

    boolean existsCompanyByCompanyFullName(String name);

    @Query("""
            select c from Company c join fetch c.industry
            left join Account a on c.account.id = a.id
            where c.deleteFlag = false and a.email = ?1
    """)
    Optional<Company> findCompanyByEmail(String email);

    @Query("""
        select c from Company c join fetch c.industry
        where c.deleteFlag = false
    """)
    List<Company> findAllCompany();
    @Query("""
        select c from Company c join fetch c.industry
        where c.deleteFlag = false and c.companyId = :id
    """)
    Optional<Company> findByCompanyId(Integer id);

//    @Query(value = "select c.company_id from company c " +
//            "right join company_branch b on c.company_id = b.company_id " +
//            "right join hr_branch h on h.branch_id = b.branch_id " +
//            "where h.user_id = ?1 " +
//            "group by c.company_id", nativeQuery = true)
//    Integer findCompanyByHR(int id);
}
