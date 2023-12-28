package com.recruitmentsystem.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
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
    private Integer categoryTotal;
}
