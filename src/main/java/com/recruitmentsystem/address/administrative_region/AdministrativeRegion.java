package com.recruitmentsystem.address.administrative_region;

import com.recruitmentsystem.address.Province.Province;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity (name = "administrative_regions")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class AdministrativeRegion {
    @Id
    private Integer administrativeRegionId;
    private String name;
    @Column(name = "name_en")
    private String nameEN;
    private String codeName;
    @Column(name = "code_name_en")
    private String codeNameEN;
    @OneToMany(mappedBy = "administrativeRegion", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Province> provinces;
}
