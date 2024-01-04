package com.recruitmentsystem.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
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
public class Interview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Integer interviewId;
    private LocalDateTime interviewTime;
    private Integer interviewType;
    private String interviewAddress;
    // 0 - chờ xác nhận, 1 - đồng ý, 2 - không, 3 - đậu, 4 - rớt
    private Integer interviewStatus;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidate_apply_job_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Recruitment recruitment;
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createDate;
    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime lastModified;
}
