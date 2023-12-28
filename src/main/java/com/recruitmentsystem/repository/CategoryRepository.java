package com.recruitmentsystem.repository;

import com.recruitmentsystem.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    boolean existsCategoryByCategoryName(String username);

    List<Category> findByCategoryNameContains(String name);

    Optional<Category> findByCategoryName(String name);
}
