package com.recruitmentsystem.address.province;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProvinceRepository extends JpaRepository<Province, String> {
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