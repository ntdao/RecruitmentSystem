package com.recruitmentsystem.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "address")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer addressId;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ward_code")
    @ToString.Exclude
    private Ward ward;
    private String address;
    private String fullAddress;
}
