package com.recruitmentsystem.skill;

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
    @Column(nullable = false)
    private String skillName;
    @Column(name = "skill_name_VI")
    private String skillNameVI;
//    @ManyToMany(mappedBy = "jobSkills")
//    @EqualsAndHashCode.Exclude
//    @ToString.Exclude
//    private Collection<Job> jobs;
//    @ManyToMany(mappedBy = "userSkills")
//    @EqualsAndHashCode.Exclude
//    @ToString.Exclude
//    private Collection<Account> users;
}
