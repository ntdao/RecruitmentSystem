package hust.seee.recruitmentsystem.repository;

import hust.seee.recruitmentsystem.entity.Company;
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
            join fetch c.account a
            where c.deleteFlag = false and a.deleteFlag = false
            """)
    List<Company> findAllCompany();

    @Query(value = """
            select c from Company c 
            left join fetch c.industry
            left join fetch c.account a
            where c.deleteFlag = false and a.deleteFlag = false
            """,
            countQuery = """
                    select c from Company c 
                    left join fetch c.industry
                    left join fetch c.account a
                    where c.deleteFlag = false and a.deleteFlag = false
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
            select c.companyShortName as companyShortName, c.companyLogo as companyLogo, c.industry.name as industry
            from Company c
            where c.deleteFlag = false
            """,
            countQuery = """
                    select c.companyShortName as companyShortName, c.companyLogo as companyLogo, c.industry.name as industry
                    from Company c
                    where c.deleteFlag = false
                    """
    )
    Page<Map<String, Object>> findTopCompany(Pageable paging);

    @Query("""
            select month(c.createDate) as month, year(c.createDate) as year, count(*) as quantity
            from Company c where c.deleteFlag = false 
            group by month(c.createDate), year(c.createDate)
            """)
    List<Map<String, Object>> getQuantity();

    @Query(value = "select count(*) from Company c where 1 = 1 " +
            "and :id is null or c.companyId <> :id " +
            "and (c.companyShortName = :shortName or c.companyFullName = :fullName)")
    Integer countByName(Integer id, String shortName, String fullName);

    @Query(value = """
            select j from Company j
            left join fetch j.companyAddress
            left join fetch j.companyAddress.ward
            left join fetch j.companyAddress.ward.district
            left join fetch j.companyAddress.ward.district.province
            where 1 = 1 and j.deleteFlag = false
            and (:name is null or lower(j.companyFullName) like %:name% or lower(j.companyShortName) like %:name%)
            and (:industryId is null or j.industry.id = :industryId)
            and (:provinceCode is null or j.companyAddress.ward.district.province.provinceCode = :provinceCode)
            """,
    countQuery = """
            select j from Company j
            left join fetch j.companyAddress
            left join fetch j.companyAddress.ward
            left join fetch j.companyAddress.ward.district
            left join fetch j.companyAddress.ward.district.province
            where 1 = 1 and j.deleteFlag = false
            and (:name is null or lower(j.companyFullName) like %:name% or lower(j.companyShortName) like %:name%)
            and (:industryId is null or j.industry.id = :industryId)
            and (:provinceCode is null or j.companyAddress.ward.district.province.provinceCode = :provinceCode)
    """)
    Page<Company> findAll(String name, Integer industryId, String provinceCode, Pageable pageable);
}
