package com.recruitmentsystem.service;

import com.recruitmentsystem.common.exception.ResourceAlreadyExistsException;
import com.recruitmentsystem.common.exception.ResourceNotFoundException;
import com.recruitmentsystem.entity.CompanyBranch;
import com.recruitmentsystem.mapper.BranchMapper;
import com.recruitmentsystem.model.branch.BranchDisplayModel;
import com.recruitmentsystem.model.branch.BranchRequestModel;
import com.recruitmentsystem.repository.IBranchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BranchService {
    private final IBranchRepository branchRepository;
    private final BranchMapper branchMapper;

    public void addBranch(BranchRequestModel request) {
        // check branch name
        String branchName = request.name();
        if (branchRepository.existsBranchByName(branchName)) {
            throw new ResourceAlreadyExistsException("branch name already taken");
        }

        // add
        CompanyBranch branch = branchMapper.branchRequestModelToBranch(request);
        branch.setCreatedAt(Instant.now());
        branchRepository.save(branch);
    }

    public List<BranchDisplayModel> findAllCompanies() {
        List<CompanyBranch> branchs = branchRepository.findAll();
        return branchs.stream()
                .filter(branch -> !branch.isDeleteFlag())
                .map(branchMapper::branchToDisplayModel)
                .collect(Collectors.toList());
    }

    public BranchDisplayModel findById(Integer id) {
        return branchRepository.findById(id)
                .filter(branch -> !branch.isDeleteFlag())
                .map(branchMapper::branchToDisplayModel)
                .orElseThrow(() -> new ResourceNotFoundException("Branch with id " + id + " does not exist"));
    }

    public CompanyBranch findBranchById(Integer id) {
        return branchRepository.findById(id)
                .filter(branch -> !branch.isDeleteFlag())
                .orElseThrow(() -> new ResourceNotFoundException("Branch with id " + id + " does not exist"));
    }

    public CompanyBranch findBranchByBranchName(String name) {
        return branchRepository.findBrandByName(name)
                .filter(branch -> !branch.isDeleteFlag())
                .orElseThrow(() -> new ResourceNotFoundException("Branch with name " + name + " does not exist"));
    }

    @Transactional
    public void updateBranch(Integer id, BranchRequestModel requestModel) {
        // tim branch theo id
        CompanyBranch updateBranch = findBranchById(id);
//        updateBranch.setUpdatedAt(LocalDateTime.now());

        // tao ban ghi luu thong tin cu cua branch
        CompanyBranch oldBranch = new CompanyBranch(updateBranch, id, true);
        branchRepository.save(oldBranch);

        // update branch
        updateBranch = branchMapper.branchRequestModelToBranch(requestModel);
        updateBranch.setBranchId(id);
        updateBranch.setCreatedAt(oldBranch.getCreatedAt());
        updateBranch.setCreatedBy(oldBranch.getCreatedBy());
        updateBranch.setUpdatedAt(oldBranch.getUpdatedAt());
//        updateBranch.setUpdatedBy();
        branchRepository.save(updateBranch);
    }

    public void deleteBranch(Integer id) {
        CompanyBranch branch = findBranchById(id);
        branch.setDeleteFlag(true);
        branchRepository.save(branch);
    }
}
