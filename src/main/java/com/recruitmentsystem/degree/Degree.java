package com.recruitmentsystem.degree;

import com.recruitmentsystem.usereducation.UserEducation;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
//@Table
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class Degree {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Integer degreeId;
    @Column(nullable = false)
    private String degreeName;
    @Column(name = "degree_name_VI")
    private String degreeNameVI;
    @OneToMany(mappedBy = "degree", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    List<UserEducation> userEducations;
}
