package com.recruitmentsystem.service;

import com.recruitmentsystem.common.exception.ResourceAlreadyExistsException;
import com.recruitmentsystem.common.exception.ResourceNotFoundException;
import com.recruitmentsystem.entity.Category;
import com.recruitmentsystem.entity.Company;
import com.recruitmentsystem.entity.User;
import com.recruitmentsystem.mapper.CompanyMapper;
import com.recruitmentsystem.model.company.CompanyDisplayModel;
import com.recruitmentsystem.model.company.CompanyRequestModel;
import com.recruitmentsystem.model.user.UserDisplayModel;
import com.recruitmentsystem.repository.ICompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
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
            throw new ResourceAlreadyExistsException("Company name already taken");
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

    public List<CompanyDisplayModel> findCompanyByCompanyName(String name) {
        return companyRepository.findAll()
                .stream()
                .filter(c -> (!c.isDeleteFlag() && c.getCompanyName().contains(name)))
                .map(companyMapper::companyToDisplayModel)
                .collect(Collectors.toList());
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

    public List<CompanyDisplayModel> getTopCompanies(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

        Page<Company> pagedResult = companyRepository.findAll(paging);

        List<CompanyDisplayModel> list = pagedResult.getContent()
                .stream()
                .filter(u -> !u.isDeleteFlag())
                .map(companyMapper::companyToDisplayModel)
                .collect(Collectors.toList());

        if (pagedResult.hasContent()) {
            return list;
        } else {
            return new ArrayList<>();
        }
    }

//    public List<CompanyDisplayModel> findTopCompany() {
//        List<Company> companies= companyRepository.findAll(Sort.by(Sort.Direction.DESC, "total"));
//        return companies.stream()
//                .filter(company -> !company.isDeleteFlag())
//                .map(companyMapper::companyToDisplayModel)
//                .collect(Collectors.toList());
//    }
}
