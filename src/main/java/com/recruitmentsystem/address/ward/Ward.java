package com.recruitmentsystem.address.ward;

import com.recruitmentsystem.address.District.District;
import com.recruitmentsystem.address.administrative_unit.AdministrativeUnit;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "wards")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class Ward {
    @Id
    @Column(columnDefinition = "varchar(20)")
    private String wardCode;
    private String fullName;
    @Column(name = "full_name_en")
    private String fullNameEN;
    private String name;
    @Column(name = "name_en")
    private String nameEN;
    private String codeName;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "district_code")
    private District district;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "administrative_unit_id")
    private AdministrativeUnit administrativeUnit;
}
