package com.recruitmentsystem.userworkinghistory;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.recruitmentsystem.degree.Degree;
import com.recruitmentsystem.jobposition.JobPosition;
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
public class UserWorkingHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Integer userWorkingHistoryId;
    @Column(nullable = false)
    private String companyName;
    @Column(nullable = false)
    private String jobName;
    @Column(nullable = false)
    private String startDate;
    @Column(nullable = false)
    private String endDate;
    @Column(columnDefinition = "text")
    private String description;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonBackReference
    private User user;
    @LastModifiedDate
    private LocalDateTime lastModified;
}
