package com.recruitmentsystem.entity;

import com.recruitmentsystem.common.myEnum.Gender;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity // khai báo với Spring Boot rằng đây là 1 entity biểu diễn table trong db
@Table
@Data // tự động khai báo getter và setter cho class
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@EqualsAndHashCode
public class User extends Audit implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Integer id;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String address;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private String imgUrl;
    private LocalDate birthday;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "role_id")
    private Role role;
    private Boolean enabled = false;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "HR_branch",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "branch_id")
    )
    private Set<CompanyBranch> branches = new HashSet<>();

//    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    @JoinTable(
//            name = "users_roles",
//            joinColumns = @JoinColumn(name = "user_id"),
//            inverseJoinColumns = @JoinColumn(name = "role_id")
//    )
//    private Set<Role> roles = new HashSet<>();

//    public void addRole(Role role) {
//        this.roles.add(role);
//    }

    public User(User user, boolean deleteFlag) {
        super(user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getCreatedBy(),
                user.getUpdatedBy(),
                deleteFlag,
                user.getId());
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.email = user.getEmail();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.phoneNumber = user.getPhoneNumber();
        this.address = user.getAddress();
        this.gender = user.getGender();
        this.imgUrl = user.getImgUrl();
        this.birthday = user.getBirthday();
        this.role = user.getRole();
        this.enabled = user.getEnabled();
    }

//    @Override
//    public String getUsername() {
//        return email;
//    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
//        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
//        for (Role role : roles) {
//            authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
//        }
//        return authorities;
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(this.role.getRoleName());
        return Collections.singletonList(authority);
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
