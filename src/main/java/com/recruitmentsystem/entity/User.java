package com.recruitmentsystem.entity;

import com.recruitmentsystem.myEnum.Gender;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class User extends Audit implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Integer id;
    @Column(nullable = false, unique = true, length = 255)
    private String username;
    @Column(nullable = false, length = 255)
    private String password;
    @Column(nullable = false, unique = true, length = 255)
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String address;
//    @Enumerated(EnumType.STRING)
    private Gender gender;
    private String imgUrl;
    private LocalDate birthday;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "roleId")
    private Role role;

    public User(String username,
                String password,
                String email,
                String firstName,
                String lastName,
                String phoneNumber,
                String address,
                Gender gender,
                String imgUrl,
                LocalDate birthday,
                Role role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.gender = gender;
        this.imgUrl = imgUrl;
        this.birthday = birthday;
        this.role = role;
    }
}
