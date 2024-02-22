package hust.seee.recruitmentsystem.repository;

import hust.seee.recruitmentsystem.entity.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface DistrictRepository extends JpaRepository<District, String> {
    @Query("""
            select d.districtCode as code, d.fullName as name from districts d 
            where d.province.provinceCode = :code
            """)
    List<Map<String, Object>> findDistrictByProvinceCode(String code);

    @Query("""
            select d.districtCode from districts d
            join wards w on w.district.districtCode = d.districtCode
            where w.wardCode = :code
            """)
    Optional<String> findDistrictByWardCode(String code);

    @Query("""
            select d.fullName from districts d
            where d.districtCode = :code
            """)
    Optional<String> findByDistrictCode(String code);
}
