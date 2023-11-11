package com.recruitmentsystem.category;

import com.recruitmentsystem.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer>, PagingAndSortingRepository<Category, Integer> {
    boolean existsCategoryByCategoryName(String username);

    Optional<Category> findByCategoryName(String name);
}
