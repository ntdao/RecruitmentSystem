package com.recruitmentsystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class Degree {
    @OneToMany(mappedBy = "degree", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    List<CandidateEducation> candidateEducations;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Integer degreeId;
    @Column(nullable = false)
    private String degreeName;
    @Column(name = "degree_name_VI")
    private String degreeNameVI;
}
