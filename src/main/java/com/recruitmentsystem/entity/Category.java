package com.recruitmentsystem.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@Table
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@ToString
public class Category extends Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Integer categoryId;
    @Column(nullable = false)
    private String categoryName;

    private String categoryIcon;

    private String categoryUrl;

    private Integer total;

    public Category(Category category, Integer oldId, boolean deleteFlag) {
        super(category.getCreatedAt(),
                category.getUpdatedAt(),
                category.getCreatedBy(),
                category.getUpdatedBy(),
                deleteFlag,
                oldId);
        this.categoryName = category.getCategoryName();
        this.categoryIcon = category.getCategoryIcon();
        this.categoryUrl = category.getCategoryUrl();
        this.total = category.getTotal();
    }
}
