package com.recruitmentsystem.category;

import jakarta.persistence.*;
import lombok.*;

@Entity
//@Table
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Integer categoryId;
    @Column(nullable = false)
    private String categoryName;
    private String categoryIcon;
//    private String categoryUrl;
    private Integer total;
    public Category (Category category) {
        this.categoryName = category.getCategoryName();
        this.categoryIcon = category.getCategoryIcon();
        this.total = category.getTotal();
    }
}
