package com.recruitmentsystem.address.ward;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WardRepository extends JpaRepository<Ward, String> {
    @Query("""
            select w from wards w 
            where w.district.districtCode = :code
            """)
    List<Ward> findAllByDistrictCode(String code);

    Optional<Ward> findByWardCode(String code);
}
