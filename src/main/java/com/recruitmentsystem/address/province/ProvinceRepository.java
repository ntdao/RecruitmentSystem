package com.recruitmentsystem.address.province;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProvinceRepository extends JpaRepository<Province, String> {
    @Query("""
            select p.provinceCode from provinces p
            right join districts d on d.province.provinceCode = p.provinceCode
            right join wards w on w.district.districtCode = d.districtCode
            where w.wardCode = :code
            """)
    Optional<String> findProvinceByWardCode(String code);

    Optional<Province> findByProvinceCode(String code);
}
