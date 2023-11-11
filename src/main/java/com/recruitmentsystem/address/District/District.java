package com.recruitmentsystem.address.District;

import com.recruitmentsystem.address.Province.Province;
import com.recruitmentsystem.address.administrative_region.AdministrativeRegion;
import com.recruitmentsystem.address.administrative_unit.AdministrativeUnit;
import com.recruitmentsystem.address.ward.Ward;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity(name = "districts")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class District {
    @Id
    @Column(columnDefinition = "varchar(20)")
    private String districtCode;
    private String fullName;
    @Column(name = "full_name_en")
    private String fullNameEN;
    private String name;
    @Column(name = "name_en")
    private String nameEN;
    private String codeName;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "province_code")
    private Province province;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "administrative_unit_id")
    private AdministrativeUnit administrativeUnit;
    @OneToMany(mappedBy = "district", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Ward> wards;
}
