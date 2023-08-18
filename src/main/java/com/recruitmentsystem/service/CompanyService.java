package com.recruitmentsystem.service;

import com.recruitmentsystem.common.exception.ResourceAlreadyExistsException;
import com.recruitmentsystem.common.exception.ResourceNotFoundException;
import com.recruitmentsystem.entity.Company;
import com.recruitmentsystem.mapper.CompanyMapper;
import com.recruitmentsystem.model.company.CompanyDisplayModel;
import com.recruitmentsystem.model.company.CompanyRequestModel;
import com.recruitmentsystem.model.pagination.MyPagination;
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
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final ICompanyRepository companyRepository;
    private final CompanyMapper companyMapper;

    private boolean checkDuplicateCompanyName(String name) {
        if (companyRepository.existsCompanyByCompanyName(name)) {
            throw new ResourceAlreadyExistsException("Company name already taken");
        }
        return false;
    }

    public void addCompanyAdmin(CompanyRequestModel request) {
        try {
            if (!checkDuplicateCompanyName(request.companyName())) {
                Company company = companyMapper.companyRequestModelToCompany(request);
                company.setCreatedAt(Instant.now());
                companyRepository.save(company);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
//        catch (ResourceAlreadyExistsException e) {
//            throw new ResourceAlreadyExistsException(e.getMessage());
//        } catch (Exception e) {
//            throw new RuntimeException(e.getMessage());
//        }
    }

    public List<CompanyDisplayModel> findAllCompanies() {
        List<Company> companies = companyRepository.findAll();
        return companies.stream()
                .filter(company -> !company.isDeleteFlag())
                .map(companyMapper::companyToDisplayModel)
                .collect(Collectors.toList());
    }

    public List<Company> findAllCompaniesAdmin() {
        List<Company> companies = companyRepository.findAll();
        return companies.stream()
                .filter(company -> !company.isDeleteFlag())
                .collect(Collectors.toList());
    }

    public Company findCompanyByIdAdmin(Integer id) {
        return companyRepository.findById(id)
                .filter(company -> !company.isDeleteFlag())
                .orElseThrow(() -> new ResourceNotFoundException("Company with id " + id + " does not exist"));
    }

    public List<Company> findCompanyByCompanyNameAdmin(String name) {
        return companyRepository.findAll()
                .stream()
                .filter(c -> (!c.isDeleteFlag() && c.getCompanyName().contains(name)))
                .collect(Collectors.toList());
    }

    public List<CompanyDisplayModel> findCompanyByCompanyName(String name) {
        return companyRepository.findAll()
                .stream()
                .filter(c -> (!c.isDeleteFlag() && c.getCompanyName().contains(name)))
                .map(companyMapper::companyToDisplayModel)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateCompanyByAdmin(Integer id, CompanyRequestModel requestModel) {
        // tim company theo id
        Company updateCompany = findCompanyByIdAdmin(id);
        updateCompany(updateCompany, requestModel, 0);
    }

    private void updateCompany(Company updateCompany,
                               CompanyRequestModel requestModel,
                               Integer updatedBy) {

        int id = updateCompany.getCompanyId();

        // tao ban ghi luu thong tin cu cua company
        Company oldCompany = new Company(updateCompany, true);
        companyRepository.save(oldCompany);

        // update company
        updateCompany = companyMapper.companyRequestModelToCompany(requestModel);
        updateCompany.setCompanyId(id);
        updateCompany.setCreatedAt(oldCompany.getCreatedAt());
        updateCompany.setCreatedBy(oldCompany.getCreatedBy());
        updateCompany.setUpdatedAt(Instant.now());
        updateCompany.setUpdatedBy(updatedBy);
        companyRepository.save(updateCompany);
    }

    public void deleteCompany(Integer id) {
        Company company = findCompanyByIdAdmin(id);
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

    public MyPagination<CompanyDisplayModel> getAllCompanies(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

        Page<Company> pagedResult = companyRepository.findAll(paging);

        List<CompanyDisplayModel> list = pagedResult.getContent()
                .stream()
                .filter(u -> !u.isDeleteFlag())
                .map(companyMapper::companyToDisplayModel)
                .collect(Collectors.toList());

        int totalPages;
        if (list.size() % pageSize == 0) {
            totalPages = list.size() / pageSize;
        } else {
            totalPages = list.size() / pageSize + 1;
        }

        MyPagination pagination = MyPagination.builder()
                .total(list.size())
                .totalPage(totalPages)
                .pageSize(pageSize)
                .pageNo(pageNo)
                .list(Collections.singletonList(list))
                .build();
        return pagination;
    }
}
