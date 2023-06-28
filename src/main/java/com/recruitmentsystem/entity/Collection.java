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
public class Collection extends Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Integer collectionId;
    @Column(nullable = false)
    private String collectionName;
}
