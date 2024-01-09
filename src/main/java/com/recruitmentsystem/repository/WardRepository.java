package com.recruitmentsystem.repository;

import com.recruitmentsystem.dto.WardDto;
import com.recruitmentsystem.entity.Ward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WardRepository extends JpaRepository<Ward, String> {
    @Query("""
            select new com.recruitmentsystem.dto.WardDto(w.wardCode, w.fullName) from wards w 
            where w.district.districtCode = :code
            """)
    List<WardDto> findAllByDistrictCode(String code);

    Optional<Ward> findByWardCode(String code);
}
