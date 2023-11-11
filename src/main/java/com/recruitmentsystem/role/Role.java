package com.recruitmentsystem.role;

import com.recruitmentsystem.account.Account;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
//@Table
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
//@ToString
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
    private List<Account> accounts;

    public Role(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public String toString() {
        return "Role{" +
                "roleId=" + roleId +
                ", roleName='" + roleName + '\'' +
                '}';
    }
}
