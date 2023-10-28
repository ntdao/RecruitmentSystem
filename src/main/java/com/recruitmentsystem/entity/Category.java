package com.recruitmentsystem.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
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

    public Category(Category category, boolean deleteFlag) {
        super(category.getCreatedAt(),
                category.getUpdatedAt(),
                category.getCreatedBy(),
                category.getUpdatedBy(),
                deleteFlag,
                category.getCategoryId());
        this.categoryName = category.getCategoryName();
        this.categoryIcon = category.getCategoryIcon();
        this.categoryUrl = category.getCategoryUrl();
        this.total = category.getTotal();
    }
}
