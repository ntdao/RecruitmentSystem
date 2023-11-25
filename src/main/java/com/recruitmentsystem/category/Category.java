package com.recruitmentsystem.category;

import com.recruitmentsystem.company.Company;
import com.recruitmentsystem.job.Job;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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
    private Integer total;
    public Category (Category category) {
        this.categoryName = category.getCategoryName();
        this.categoryIcon = category.getCategoryIcon();
        this.total = category.getTotal();
    }
}
