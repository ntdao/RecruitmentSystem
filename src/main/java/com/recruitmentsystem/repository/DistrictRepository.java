package com.recruitmentsystem.repository;

import com.recruitmentsystem.dto.DistrictDto;
import com.recruitmentsystem.entity.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DistrictRepository extends JpaRepository<District, String> {
    @Query("""
            select new com.recruitmentsystem.dto.DistrictDto(d.districtCode, d.fullName) from districts d 
            where d.province.provinceCode = :code
            """)
    List<DistrictDto> findDistrictByProvinceCode(String code);

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
