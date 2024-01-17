package com.recruitmentsystem.repository;

import com.recruitmentsystem.dto.CompanyTopModel;
import com.recruitmentsystem.entity.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {

    boolean existsCompanyByCompanyShortName(String name);

    boolean existsCompanyByCompanyFullName(String name);

    @Query("""
            select c from Company c 
            left join fetch c.industry
            join fetch c.account a
            where c.deleteFlag = false and a.email = ?1
            """)
    Optional<Company> findCompanyByEmail(String email);

    @Query("""
            select c from Company c 
            left join fetch c.industry
            left join fetch c.companyAddress
            join fetch c.account
            where c.deleteFlag = false
            """)
    List<Company> findAllCompany();

    @Query(value = """
            select c from Company c 
            left join fetch c.industry
            join fetch c.account
            where c.deleteFlag = false
            """,
            countQuery = """
                    select c from Company c 
                    left join fetch c.industry
                    where c.deleteFlag = false
                    """)
    Page<Company> findAllCompany(Pageable pageable);

    @Query("""
            select c from Company c 
            left join fetch c.industry
            join fetch c.account
            where c.deleteFlag = false 
            and c.companyId = :id
            """)
    Optional<Company> findByCompanyId(Integer id);

    @Query("""
            select c from Company c 
            left join fetch c.industry
            join fetch c.account
            where c.deleteFlag = false
            and (c.companyShortName like %:name%
            or c.companyFullName like %:name%)
            """)
    List<Company> findByName(String name);

    @Query(value = """
            select new com.recruitmentsystem.dto.CompanyTopModel(c.companyShortName, c.companyLogo, c.industry.industryNameVI) 
            from Company c
            where c.deleteFlag = false
            """,
            countQuery = """
                    select new com.recruitmentsystem.dto.CompanyTopModel(c.companyShortName, c.companyLogo, c.industry.industryNameVI) 
                    from Company c
                    where c.deleteFlag = false
                    """
    )
    Page<CompanyTopModel> findTopCompany(Pageable paging);

    @Query("""
            select month(c.createDate) as month, year(c.createDate) as year, count(*) as quantity
            from Company c where c.deleteFlag = false 
            group by month(c.createDate), year(c.createDate)
            """)
    List<Map<String, Object>> getQuantity();
}
