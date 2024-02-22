package hust.seee.recruitmentsystem.repository;

import hust.seee.recruitmentsystem.dto.JobDTO;
import hust.seee.recruitmentsystem.entity.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface JobRepository extends JpaRepository<Job, Integer> {
    @Query("""
            select j from Job j
            left join fetch j.jobSkills
            left join fetch j.jobAddress
            where j.company.companyId = :id
            and j.deleteFlag = false
            """)
    List<Job> findAllJobByCompany(Integer id);

    @Query(value = """
            select j from Job j
            left join fetch j.jobSkills
            left join fetch j.jobAddress
            where j.deleteFlag = false
            and j.jobName like %?1%
            """,
            countQuery = """
                    select j from Job j
                    left join fetch j.jobSkills
                    left join fetch j.jobAddress
                    where j.deleteFlag = false
                    and j.jobName like %?1%
                    """)
    Page<Job> findJobByName(String name, Pageable paging);

    @Query("""
            select j from Job j
            left join fetch j.jobSkills
            left join fetch j.jobAddress
            where j.deleteFlag = false
            and j.jobId = :id
            """)
    Optional<Job> findById(Integer id);

    @Query(value = """
            select new hust.seee.recruitmentsystem.dto.JobDTO(
            j.jobName, j.company.companyShortName, j.company.companyLogo, j.salary)
            from Job j 
            where j.deleteFlag = false and j.jobStatus = 0
            """,
            countQuery = """
                    select new hust.seee.recruitmentsystem.dto.JobDTO(
                    j.jobName, j.company.companyShortName, j.company.companyLogo, j.salary)
                    from Job j 
                    where j.deleteFlag = false and j.jobStatus = 0
                    """)
    Page<JobDTO> findTopJob(Pageable paging);

    @Query("""
            select j from Job j
            left join fetch j.jobSkills
            left join fetch j.jobAddress
            where j.deleteFlag = false
            """)
    List<Job> findAllJob();

    @Query("""
            select j from Job j
            left join fetch j.jobSkills
            left join fetch j.jobAddress
            where j.deleteFlag = false and j.category.name = :categoryName
            """)
    List<Job> findAllJobByCategory(String categoryName);

    @Query(value = """
            select j from Job j
            left join fetch j.jobSkills
            left join fetch j.jobAddress
            where j.deleteFlag = false
            and j.jobName like %?1%
            and j.category.id in ?2
            """,
            countQuery = """
                    select j from Job j
                    left join fetch j.jobSkills
                    left join fetch j.jobAddress
                    where j.deleteFlag = false
                    and j.jobName like %?1%
                    and j.category.id in ?2
                    """)
    Page<Job> findJobByNameAndCategory(String name, List<Integer> categories, Pageable paging);

    @Query("""
            select j from Job j
            left join fetch j.jobSkills
            left join fetch j.jobAddress
            where j.deleteFlag = false
            and j.jobName like %?1%
            """)
    List<Job> findByName(String name);

    @Query("""
            select j.jobId as jobId, j.jobName as jobName
            from Job j
            where j.jobStatus in :jobStatus
            and j.company.companyId = :companyId
            """)
    List<Map<String, Object>> findJobByStatus(List<Integer> jobStatus, Integer companyId);

    @Query("""
            select month(j.createDate) as month, year(j.createDate) as year, count(*) as quantity
            from Job j where j.deleteFlag = false 
            group by month(j.createDate), year(j.createDate)
            """)
    List<Map<String, Object>> getQuantity();

    @Query(value = """
            select j from Job j
            left join fetch j.jobSkills
            left join fetch j.jobAddress
            left join fetch j.jobAddress.ward
            left join fetch j.jobAddress.ward.district
            left join fetch j.jobAddress.ward.district.province
            where 1 = 1 and j.deleteFlag = false
            and (:name is null or lower(j.jobName) like %:name%)
            and (:categoryId is null or j.category.id = :categoryId)
            and (:jobTypeId is null or j.jobType.id = :jobTypeId)
            and (:provinceCode is null or j.jobAddress.ward.district.province.provinceCode = :provinceCode)
            and (:jobStatus is null or j.jobStatus = :jobStatus)
            and (:companyId is null or j.company.companyId = :companyId)
            """,
    countQuery = """
            select j from Job j
            left join fetch j.jobSkills
            left join fetch j.jobAddress
            left join fetch j.jobAddress.ward
            left join fetch j.jobAddress.ward.district
            left join fetch j.jobAddress.ward.district.province
            where 1 = 1 and j.deleteFlag = false
            and (:name is null or lower(j.jobName) like %:name%)
            and (:categoryId is null or j.category.id = :categoryId)
            and (:jobTypeId is null or j.jobType.id = :jobTypeId)
            and (:provinceCode is null or j.jobAddress.ward.district.province.provinceCode = :provinceCode)
            and (:jobStatus is null or j.jobStatus = :jobStatus)
            and (:companyId is null or j.company.companyId = :companyId)
    """)
    Page<Job> findAll(String name, Integer categoryId, Integer jobTypeId, String provinceCode, Integer jobStatus, Integer companyId, Pageable pageable);
}
