package com.recruitmentsystem.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class Role extends Audit implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Integer roleId;
    @Column(nullable = false)
    private String roleName;

    public Role(String roleName) {
        this.roleName = roleName;
    }

    public Role(Role role, boolean deleteFlag) {
        super(role.getCreatedAt(),
                role.getUpdatedAt(),
                role.getCreatedBy(),
                role.getUpdatedBy(),
                deleteFlag,
                role.getRoleId());
        this.roleName = role.getRoleName();
    }

    @Override
    public String toString() {
        return this.roleName;
    }
}
