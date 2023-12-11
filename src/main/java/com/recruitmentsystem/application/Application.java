package com.recruitmentsystem.application;

import com.recruitmentsystem.job.Job;
import com.recruitmentsystem.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "user_apply_job")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer applicationId;
//    @Column(nullable = false)
//    private String cvUrl;
    private Integer status;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id")
    @ToString.Exclude
    private Job job;
}
