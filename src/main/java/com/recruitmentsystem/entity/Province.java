package com.recruitmentsystem.entity;

import com.recruitmentsystem.entity.AdministrativeUnit;
import com.recruitmentsystem.entity.District;
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
    @ToString.Exclude
    private AdministrativeUnit administrativeUnit;
    @OneToMany(mappedBy = "province", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<District> districts;
}
