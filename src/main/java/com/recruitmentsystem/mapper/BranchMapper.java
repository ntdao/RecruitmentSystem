package com.recruitmentsystem.mapper;

import com.recruitmentsystem.entity.CompanyBranch;
import com.recruitmentsystem.model.branch.BranchDisplayModel;
import com.recruitmentsystem.model.branch.BranchRequestModel;
import org.springframework.stereotype.Service;

@Service
public class BranchMapper {
    public BranchDisplayModel branchToDisplayModel(CompanyBranch branch) {
        return BranchDisplayModel
                .builder()
                .id(branch.getBranchId())
                .name(branch.getBranchName())
                .companyLogo(branch.getCompany().getCompanyLogo())
                .branchAddress(branch.getBranchAddress())
                .createdAt(branch.getCreatedAt())
                .updatedAt(branch.getUpdatedAt())
                .build();
    }

    public CompanyBranch branchRequestModelToBranch(BranchRequestModel request) {
        return CompanyBranch
                .builder()
                .branchName(request.name())
//                .branchLogo(request.branchLogo())
                .branchAddress(request.branchAddress())
                .build();
    }
}
