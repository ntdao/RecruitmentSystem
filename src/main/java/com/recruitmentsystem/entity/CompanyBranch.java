package com.recruitmentsystem.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class CompanyBranch extends Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Integer branchId;
    @Column(nullable = false)
    private String branchName;
    @Column(nullable = false)
    private String branchAddress;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "company_id")
    private Company company;

    public CompanyBranch(CompanyBranch branch, boolean deleteFlag) {
        super(branch.getCreatedAt(),
                branch.getUpdatedAt(),
                branch.getCreatedBy(),
                branch.getUpdatedBy(),
                deleteFlag,
                branch.getBranchId());
        this.branchName = branch.getBranchName();
        this.branchAddress = branch.getBranchAddress();
    }
}
