//package com.recruitmentsystem.entity;
//
//import jakarta.persistence.*;
//import lombok.*;
//
//import java.util.Set;
//
//@Entity
//@Table
//@NoArgsConstructor
//@AllArgsConstructor
//@Getter
//@Setter
//@ToString
//@Builder
//public class Benefit extends Audit {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(updatable = false)
//    private Integer benefitId;
//    @Column(nullable = false)
//    private String benefitName;
//    @Column(nullable = false)
//    private String benefitIcon;
//    @OneToMany(mappedBy = "benefit")
//    Set<CompanyBenefit> benefitDetail;
//}
