package com.recruitmentsystem.address.District;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DistrictRepository extends JpaRepository<District, String> {
    @Query("""
        select d from districts d 
        where d.province.provinceCode = :code
    """)
    List<District> findAllByProvinceCode(String code);
}
