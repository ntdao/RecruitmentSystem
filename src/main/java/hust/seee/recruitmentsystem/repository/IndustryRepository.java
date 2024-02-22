package hust.seee.recruitmentsystem.repository;

import hust.seee.recruitmentsystem.entity.Industry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface IndustryRepository extends JpaRepository<Industry, Integer> {
    @Query("""
            select r from Industry r where 1 = 1
            and (:name is null or lower(r.code) like %:name% or lower(r.name) like %:name%)
            """)
    Page<Industry> findAll(String name, Pageable pageable);

    @Query(value = "select count(*) from Industry r where 1 = 1 " +
            "and (:id is null or r.id != :id) " +
            "and ((:name is null or lower(r.name) like :name) " +
            "or (lower(r.code) like :code))")
    Integer countByNameAndCode(Integer id, String name, String code);

    Optional<Industry> findByName(String name);
}

