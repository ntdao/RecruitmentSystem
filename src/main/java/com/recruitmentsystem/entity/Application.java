package com.recruitmentsystem.entity;

import com.recruitmentsystem.entity.Job;
import com.recruitmentsystem.entity.User;
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
