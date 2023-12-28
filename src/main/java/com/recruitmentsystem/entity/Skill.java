package com.recruitmentsystem.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class Skill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Integer skillId;
    private String skillName;
    @Column(name = "skill_name_VI")
    private String skillNameVI;
}
