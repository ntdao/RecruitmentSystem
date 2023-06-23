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
public class CompanyBranch extends Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Integer branchId;
    @Column(nullable = false)
    private  String branchName;
    @Column(nullable = false)
    private String branchImageUrl;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "company_id")
    private Company company;
}
