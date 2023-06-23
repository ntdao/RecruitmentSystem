package com.recruitmentsystem.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@Builder
public class Job extends Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Integer jobId;

    @Column(nullable = false)
    private String jobName;
    @Column(nullable = false)
    private String specFile;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "branch_id")
    private CompanyBranch branch;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "collection_id")
    private Collection collection;
}
