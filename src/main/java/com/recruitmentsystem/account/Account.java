package com.recruitmentsystem.account;

import com.recruitmentsystem.role.Role;
import com.recruitmentsystem.token.Token;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Account implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String email;
    private String password;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    @ToString.Exclude
    private Role role;
//    @CreatedDate
//    @Column(nullable = false, updatable = false)
//    private LocalDateTime createDate;
//    @LastModifiedDate
//    @Column(insertable = false)
//    private LocalDateTime lastModified;
//    @CreatedBy
//    @Column(nullable = false, updatable = false)
//    private Integer createdBy;
//    @LastModifiedBy
//    @Column(insertable = false)
//    private Integer lastModifiedBy;
    private boolean deleteFlag = false;
    private Integer oldId;
    private boolean enabled = false;
    @OneToMany(mappedBy = "account")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Token> tokens;
    public Account(Account account, boolean deleteFlag) {
        this.email = account.getEmail();
        this.password = account.getPassword();
        this.role = account.getRole();
//        this.createDate = account.getCreateDate();
//        this.lastModified = account.getLastModified();
//        this.createdBy = account.getCreatedBy();
//        this.lastModifiedBy = account.getLastModifiedBy();
        this.deleteFlag = deleteFlag;
        this.oldId = account.getId();
        this.enabled = account.isEnabled();
//        this.tokens = account.getTokens();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(this.role.getRoleName());
        return Collections.singletonList(authority);
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}