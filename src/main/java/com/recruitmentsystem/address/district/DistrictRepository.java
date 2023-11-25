package com.recruitmentsystem.address.district;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DistrictRepository extends JpaRepository<District, String> {
    @Query("""
            select d from districts d 
            where d.province.provinceCode = :code
            """)
    List<District> findDistrictByProvinceCode(String code);

    @Query("""
            select d.districtCode from districts d
            right join wards w on w.district.districtCode = d.districtCode
            where w.wardCode = :code
            """)
    Optional<String> findDistrictByWardCode(String code);

    Optional<District> findByDistrictCode(String code);
}
