package com.recruitmentsystem.repository;

import com.recruitmentsystem.dto.ProvinceDto;
import com.recruitmentsystem.entity.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProvinceRepository extends JpaRepository<Province, String> {
    @Query("""
            select new com.recruitmentsystem.dto.ProvinceDto(p.provinceCode, p.fullName)
            from provinces p
            """)
    List<ProvinceDto> findAllProvince();
    @Query("""
            select p from provinces p
            right join districts d on d.province.provinceCode = p.provinceCode
            right join wards w on w.district.districtCode = d.districtCode
            where w.wardCode = :code
            """)
    Optional<Province> findProvinceByWardCode(String code);

    @Query("""
            select p.fullName from provinces p
            where p.provinceCode = :code
            """)
    Optional<String> findByProvinceCode(String code);
}