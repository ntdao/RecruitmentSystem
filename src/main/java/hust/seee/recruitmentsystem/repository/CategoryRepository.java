package hust.seee.recruitmentsystem.repository;

import hust.seee.recruitmentsystem.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    @Query("""
            select r from Category r where 1 = 1
            and (:name is null or lower(r.code) like %:name% or lower(r.name) like %:name%)
            """)
    Page<Category> findAll(String name, Pageable pageable);

    @Query(value = "select count(*) from Category r where 1 = 1 " +
            "and (:id is null or r.id != :id) " +
            "and ((:name is null or lower(r.name) like :name) " +
            "or (lower(r.code) like :code))")
    Integer countByNameAndCode(Integer id, String name, String code);

    Optional<Category> findByName(String name);
}

