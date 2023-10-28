//package com.recruitmentsystem.entity;
//
//import jakarta.persistence.*;
//import lombok.*;
//
//@Entity
//@Table
//@NoArgsConstructor
//@AllArgsConstructor
//@Getter
//@Setter
//@ToString
//@Builder
//public class CompanyBenefit extends Audit {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(updatable = false)
//    private Integer id;
//    @ManyToOne
//    @JoinColumn(name = "company_id")
//    Company company;
//    @ManyToOne
//    @JoinColumn(name = "benefit_id")
//    private Benefit benefit;
//    private String benefitDescription;
//}
