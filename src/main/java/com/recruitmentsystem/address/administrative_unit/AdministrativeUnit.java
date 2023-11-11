package com.recruitmentsystem.address.administrative_unit;

import com.recruitmentsystem.address.District.District;
import com.recruitmentsystem.address.Province.Province;
import com.recruitmentsystem.address.ward.Ward;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity(name = "administrative_units")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class AdministrativeUnit {
    @Id
    private Integer administrativeUnitId;
    private String fullName;
    @Column(name = "full_name_en")
    private String fullNameEN;
    private String shortName;
    @Column(name = "short_name_en")
    private String shortNameEN;
    private String codeName;
    @Column(name = "code_name_en")
    private String codeNameEN;
    @OneToMany(mappedBy = "administrativeUnit", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Province> provinces;
    @OneToMany(mappedBy = "administrativeUnit", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<District> districts;
    @OneToMany(mappedBy = "administrativeUnit", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Ward> wards;
}
