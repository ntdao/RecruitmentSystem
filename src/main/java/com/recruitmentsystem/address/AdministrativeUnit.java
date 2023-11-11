package com.recruitmentsystem.address;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

//@Entity(name = "administrative_units")
//@NoArgsConstructor
//@AllArgsConstructor
//@Getter
//@Setter
//@ToString
//@Builder
public class AdministrativeUnit {
//    @Id
    private Integer id;
    private String fullName;
//    @Column(name = "full_name_en")
    private String fullNameEN;
    private String shortName;
//    @Column(name = "short_name_en")
    private String shortNameEN;
    private String codeName;
//    @Column(name = "code_name_en")
    private String codeNameEN;
}
