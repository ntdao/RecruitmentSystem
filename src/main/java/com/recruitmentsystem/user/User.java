package com.recruitmentsystem.user;

import com.recruitmentsystem.account.Account;
import com.recruitmentsystem.common.myEnum.Gender;
import com.recruitmentsystem.skill.Skill;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
//@Table
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@EntityListeners(AuditingEntityListener.class)
//@EqualsAndHashCode(callSuper = true)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(updatable = false, columnDefinition = "varchar(36)")
//    @Type(type="uuid-char")
//    private UUID userId;
    private Integer userId;
    //    @Column(nullable = false)
//    private String username;
//    @Column(nullable = false)
//    private String password;
    @OneToOne(cascade = CascadeType.MERGE)
    private Account account;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String address;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private String imgUrl;
    private LocalDate birthday;
//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "role_id")
//    @ToString.Exclude
//    private Role role;
//    private boolean enabled = false;
//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
//    @EqualsAndHashCode.Exclude
//    @ToString.Exclude
//    private List<Token> tokens;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JoinTable(
            name = "user_skill",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private List<Skill> userSkills;
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createDate;
    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime lastModified;
    @CreatedBy
    @Column(nullable = false, updatable = false)
    private Integer createdBy;
    @LastModifiedBy
    @Column(insertable = false)
    private Integer lastModifiedBy;
    private boolean deleteFlag = false;
    private Integer oldId;

//    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    @JoinTable(
//            name = "HR_branch",
//            joinColumns = @JoinColumn(name = "user_id"),
//            inverseJoinColumns = @JoinColumn(name = "branch_id")
//    )
//    private Set<CompanyBranch> branches = new HashSet<>();

//    @Transient
//    public String getImgUrl() {
//        if (imgUrl == null) return null;
////        return "/image/user_profile/" + id + "/" + imgUrl;
//        return id + "/" + imgUrl;
//    }

    public User(User user, boolean deleteFlag) {
//        super(user.getEmail(),
//                user.getPassword(),
//                user.getRole());
//        this.username = user.getUsername();
//        this.password = user.getPassword();
//        this.email = user.getEmail();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.phoneNumber = user.getPhoneNumber();
        this.address = user.getAddress();
        this.gender = user.getGender();
        this.imgUrl = user.getImgUrl();
        this.birthday = user.getBirthday();
        this.account = user.getAccount();
//        this.role = user.getRole();
//        this.enabled = user.isEnabled();
//        this.userSkills = user.getUserSkills();
        this.createDate = user.getCreateDate();
        this.lastModified = user.getLastModified();
        this.createdBy = user.getCreatedBy();
        this.lastModifiedBy = user.getLastModifiedBy();
        this.deleteFlag = deleteFlag;
        this.oldId = user.getUserId();
    }

//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(this.role.getRoleName());
//        return Collections.singletonList(authority);
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return enabled;
//    }
}
