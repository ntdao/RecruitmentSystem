package com.recruitmentsystem.address.address;

import com.recruitmentsystem.address.ward.Ward;
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
    private Ward ward;
    private String address;
    private String fullAddress;
}
