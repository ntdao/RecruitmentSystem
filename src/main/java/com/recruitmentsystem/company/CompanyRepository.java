package com.recruitmentsystem.company;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {

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

    @Query(value = "select c from Company c join fetch c.industry " +
            "where c.deleteFlag = false",
            countQuery = "select c from Company c join fetch c.industry " +
                    "where c.deleteFlag = false")
    Page<Company> findAllCompany(Pageable pageable);

    @Query("""
                select c from Company c join fetch c.industry
                where c.deleteFlag = false and c.companyId = :id
            """)
    Optional<Company> findByCompanyId(Integer id);

    @Query("""
            select c from Company c 
            join fetch c.industry
            where c.deleteFlag = false
            and (c.companyShortName like %:name%
            or c.companyFullName like %:name%)
            """)
    List<Company> findByName(String name);

    @Query(value = """
            select new com.recruitmentsystem.company.CompanyTopModel(c.companyShortName, c.companyLogo, c.companyUrl, c.industry.industryNameVI) 
            from Company c
            where c.deleteFlag = false
            """,
            countQuery = """
            select new com.recruitmentsystem.company.CompanyTopModel(c.companyShortName, c.companyLogo, c.companyUrl, c.industry.industryNameVI) 
            from Company c
            where c.deleteFlag = false
    """)
    Page<CompanyTopModel> findTopCompany(Pageable paging);
}
