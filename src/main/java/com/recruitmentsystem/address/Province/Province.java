package com.recruitmentsystem.address.Province;

import com.recruitmentsystem.address.District.District;
import com.recruitmentsystem.address.administrative_region.AdministrativeRegion;
import com.recruitmentsystem.address.administrative_unit.AdministrativeUnit;
import com.recruitmentsystem.address.ward.Ward;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity(name = "provinces")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class Province {
    @Id
    @Column(columnDefinition = "varchar(20)")
    private String provinceCode;
    private String fullName;
    @Column(name = "full_name_en")
    private String fullNameEN;
    private String name;
    @Column(name = "name_en")
    private String nameEN;
    private String codeName;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "administrative_unit_id")
    private AdministrativeUnit administrativeUnit;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "administrative_region_id")
    private AdministrativeRegion administrativeRegion;
    @OneToMany(mappedBy = "province", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<District> districts;
}
