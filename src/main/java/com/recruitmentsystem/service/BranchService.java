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

import java.security.Principal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BranchService {
    private final IBranchRepository branchRepository;
    private final BranchMapper branchMapper;
    private final UserService userService;

    public void addBranch(BranchRequestModel request, Principal connectedUser) {
        // check branch name
        String branchName = request.name();
        if (branchRepository.existsByBranchName(branchName)) {
            throw new ResourceAlreadyExistsException("branch name already taken");
        }

        // add
        CompanyBranch branch = branchMapper.branchRequestModelToBranch(request);
        branch.setCreatedAt(Instant.now());
        branch.setCreatedBy(userService.getCurrentUser(connectedUser).getId());
        branchRepository.save(branch);
    }

    public List<CompanyBranch> findAllBranchesAdmin() {
        List<CompanyBranch> branchs = branchRepository.findAll();
        return branchs.stream()
                .filter(branch -> !branch.isDeleteFlag())
                .collect(Collectors.toList());
    }

//    public List<BranchDisplayModel> findAllBranches() {
//        List<CompanyBranch> branchs = branchRepository.findAll();
//        return branchs.stream()
//                .filter(branch -> !branch.isDeleteFlag())
//                .map(branchMapper::branchToDisplayModel)
//                .collect(Collectors.toList());
//    }

    public List<CompanyBranch> findAllBranchesByCompany(Integer id) {
        List<CompanyBranch> branches = branchRepository.findAll();
        System.out.println(branches);
        return branches.stream()
                .filter(branch ->
                        (!branch.isDeleteFlag() &&
                                branch.getCompany().getCompanyId() == id))
                .collect(Collectors.toList());
    }

    public List<String> findAllBranchesAddressByCompany(Integer id) {
        List<CompanyBranch> branches = branchRepository.findAll()
                .stream()
                .filter(branch ->
                        (!branch.isDeleteFlag() &&
                                branch.getCompany().getCompanyId() == id))
                .collect(Collectors.toList());
        List<String> branchAddress = new ArrayList<>();
        branches.forEach(b -> branchAddress.add(b.getBranchAddress()));
        return branchAddress;
    }

    public CompanyBranch findByIdAdmin(Integer id) {
        return branchRepository.findById(id)
                .filter(branch -> !branch.isDeleteFlag())
                .orElseThrow(() -> new ResourceNotFoundException("Branch with id " + id + " does not exist"));
    }

    public CompanyBranch findBranchById(Integer id) {
        return branchRepository.findById(id)
                .filter(branch -> !branch.isDeleteFlag())
                .orElseThrow(() -> new ResourceNotFoundException("Branch with id " + id + " does not exist"));
    }

    public List<CompanyBranch> findBranchByBranchNameAdmin(String name) {
        return branchRepository.findAll()
                .stream()
                .filter(b -> (!b.isDeleteFlag() && b.getBranchName().contains(name)))
                .collect(Collectors.toList());
    }

    public List<BranchDisplayModel> findBranchByBranchName(String name) {
        return branchRepository.findAll()
                .stream()
                .filter(b -> (!b.isDeleteFlag() && b.getBranchName().contains(name)))
                .map(branchMapper::branchToDisplayModel)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateBranch(Integer id, BranchRequestModel requestModel, Principal connectedUser) {
        // tim branch theo id
        CompanyBranch updateBranch = findBranchById(id);

        // tao ban ghi luu thong tin cu cua branch
        CompanyBranch oldBranch = new CompanyBranch(updateBranch, true);
        branchRepository.save(oldBranch);

        // update branch
        updateBranch = branchMapper.branchRequestModelToBranch(requestModel);
        updateBranch.setBranchId(id);
        updateBranch.setCreatedAt(oldBranch.getCreatedAt());
        updateBranch.setCreatedBy(oldBranch.getCreatedBy());
        updateBranch.setUpdatedAt(Instant.now());
        updateBranch.setUpdatedBy(userService.getCurrentUser(connectedUser).getId());
        branchRepository.save(updateBranch);
    }

    public void deleteBranch(Integer id, Principal connectedUser) {
        CompanyBranch branch = findBranchById(id);
        branch.setDeleteFlag(true);
        branch.setUpdatedAt(Instant.now());
        branch.setUpdatedBy(userService.getCurrentUser(connectedUser).getId());
        branchRepository.save(branch);
    }
}
