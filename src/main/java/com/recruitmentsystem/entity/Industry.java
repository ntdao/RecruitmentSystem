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
public class Industry extends Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Integer industryId;
    @Column(nullable = false)
    private String industryNameEn;
    @Column(nullable = false)
    private String industryNameVn;
}
