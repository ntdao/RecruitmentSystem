package com.recruitmentsystem.repository;

import com.recruitmentsystem.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICategoryRepository extends JpaRepository<Category, Integer> {
    boolean existsCategoryByCategoryName(String username);
}
