package com.recruitmentsystem.service.impl;

import com.recruitmentsystem.common.exception.ResourceAlreadyExistsException;
import com.recruitmentsystem.common.exception.ResourceNotFoundException;
import com.recruitmentsystem.entity.Company;
import com.recruitmentsystem.mapper.CompanyMapper;
import com.recruitmentsystem.model.company.CompanyDisplayModel;
import com.recruitmentsystem.model.company.CompanyRequestModel;
import com.recruitmentsystem.repository.ICompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final ICompanyRepository companyRepository;
    private final CompanyMapper companyMapper;

    public void addCompany(CompanyRequestModel companyRegistrationRequest) {
        // check company name
        String companyName = companyRegistrationRequest.name();
        if (companyRepository.existsCompanyByCompanyName(companyName)) {
            throw new ResourceAlreadyExistsException("company name already taken");
        }

        // add
        Company company = companyMapper.companyRequestModelToCompany(companyRegistrationRequest);
        company.setCreatedAt(Instant.now());
        companyRepository.save(company);
    }

    public List<CompanyDisplayModel> findAllCompanies() {
        List<Company> companys = companyRepository.findAll();
        return companys.stream()
                .filter(company -> !company.isDeleteFlag())
                .map(companyMapper::companyToDisplayModel)
                .collect(Collectors.toList());
    }

    public CompanyDisplayModel findById(Integer id) {
        return companyRepository.findById(id)
                .filter(company -> !company.isDeleteFlag())
                .map(companyMapper::companyToDisplayModel)
                .orElseThrow(() -> new ResourceNotFoundException("Company with id " + id + " does not exist"));
    }

    public Company findCompanyById(Integer id) {
        return companyRepository.findById(id)
                .filter(company -> !company.isDeleteFlag())
                .orElseThrow(() -> new ResourceNotFoundException("Company with id " + id + " does not exist"));
    }

    public Company findCompanyByCompanyName(String companyname) {
        return companyRepository.findCompanyByCompanyName(companyname)
                .filter(company -> !company.isDeleteFlag())
                .orElseThrow(() -> new ResourceNotFoundException("Company with companyname " + companyname + " does not exist"));
    }

    @Transactional
    public void updateCompany(Integer id, CompanyRequestModel requestModel) {
        // tim company theo id
        Company updateCompany = findCompanyById(id);
//        updateCompany.setUpdatedAt(LocalDateTime.now());

        // tao ban ghi luu thong tin cu cua company
        Company oldCompany = new Company(updateCompany, id, true);
        companyRepository.save(oldCompany);

        // update company
        updateCompany = companyMapper.companyRequestModelToCompany(requestModel);
        updateCompany.setCompanyId(id);
        updateCompany.setCreatedAt(oldCompany.getCreatedAt());
        updateCompany.setCreatedBy(oldCompany.getCreatedBy());
        updateCompany.setUpdatedAt(oldCompany.getUpdatedAt());
//        updateCompany.setUpdatedBy();
        companyRepository.save(updateCompany);
    }

    public void deleteCompany(Integer id) {
        Company company = findCompanyById(id);
        company.setDeleteFlag(true);
        companyRepository.save(company);
    }
}
