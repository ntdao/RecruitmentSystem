package com.recruitmentsystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.recruitmentsystem.entity.Account;
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
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Integer roleId;
    @Column(nullable = false)
    private String roleName;
    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    private List<Account> accounts;

    public Role(String roleName) {
        this.roleName = roleName;
    }
}
