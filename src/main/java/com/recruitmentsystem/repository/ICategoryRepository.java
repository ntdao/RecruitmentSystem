package com.recruitmentsystem.repository;

import com.recruitmentsystem.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICategoryRepository extends JpaRepository<Category, Integer>, PagingAndSortingRepository<Category, Integer> {
    boolean existsCategoryByCategoryName(String username);
}
