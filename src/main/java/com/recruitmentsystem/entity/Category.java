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

    private String category_icon;

    private String category_url;

    private Integer total;
}
