package hust.seee.recruitmentsystem.repository;

import hust.seee.recruitmentsystem.entity.Ward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface WardRepository extends JpaRepository<Ward, String> {
    @Query("""
            select w.wardCode as code, w.fullName as name from wards w 
            where w.district.districtCode = :code
            """)
    List<Map<String, Object>> findAllByDistrictCode(String code);

    Optional<Ward> findByWardCode(String code);
}
