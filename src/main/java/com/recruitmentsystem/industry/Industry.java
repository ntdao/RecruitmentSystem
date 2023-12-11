package com.recruitmentsystem.industry;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.recruitmentsystem.company.Company;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class Industry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Integer industryId;
    @Column(nullable = false)
    private String industryName;
    @Column(nullable = false, name = "industry_name_vi")
    private String industryNameVI;
    @OneToMany(mappedBy = "industry", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    private List<Company> companies;
}
