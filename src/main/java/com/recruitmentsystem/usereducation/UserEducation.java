package com.recruitmentsystem.usereducation;

import com.recruitmentsystem.degree.Degree;
import com.recruitmentsystem.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@EntityListeners(AuditingEntityListener.class)
public class UserEducation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Integer userEducationId;
    @Column(nullable = false)
    private String schoolName;
    @Column(nullable = false)
    private String major;
    @Column(nullable = false)
    private String startDate;
    @Column(nullable = false)
    private String endDate;
    private String description;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "degree_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Degree degree;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private User user;
    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime lastModified;
}
