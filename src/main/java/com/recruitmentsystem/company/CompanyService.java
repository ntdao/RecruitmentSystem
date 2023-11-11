package com.recruitmentsystem.company;

import com.recruitmentsystem.common.exception.ResourceAlreadyExistsException;
import com.recruitmentsystem.common.exception.ResourceNotFoundException;
import com.recruitmentsystem.account.Account;
import com.recruitmentsystem.pagination.MyPagination;
import com.recruitmentsystem.account.AccountService;
import com.recruitmentsystem.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final AccountService accountService;
    private final UserService userService;
    private final CompanyMapper companyMapper;

    private boolean checkDuplicateCompanyName(String shortName, String fullName) {
        if (companyRepository.existsCompanyByCompanyShortName(shortName)) {
            throw new ResourceAlreadyExistsException("Company short name already taken");
        }
        if (companyRepository.existsCompanyByCompanyFullName(fullName)) {
            throw new ResourceAlreadyExistsException("Company full name already taken");
        }
        return false;
    }

    public void addCompanyAdmin(CompanyRequestModel request, Principal connectedUser) {
        try {
            if (!checkDuplicateCompanyName(request.companyShortName(), request.companyFullName())) {
                Company company = companyMapper.companyRequestModelToCompany(request);
//                company.setCreatedAt(Instant.now());
//                company.setCreatedBy(userService.getCurrentUser(connectedUser).getUserId());
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

    public List<CompanyResponseModel> findAllCompanies() {
        List<Company> companies = companyRepository.findAllCompany();
        return companies.stream()
                .map(companyMapper::companyToResponseModel)
                .collect(Collectors.toList());
    }

    public List<Company> findAllCompaniesAdmin() {
        return companyRepository.findAllCompany();
    }

    public Company findCompanyById(Integer id) {
        return companyRepository.findByCompanyId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company with id " + id + " does not exist"));
    }

    public Company findCompanyByEmail(String email) {
        return companyRepository.findCompanyByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Company with email " + email + " does not exist"));
    }

    public CompanyResponseModel findCompanyDisplayModelById(Integer id) {
        return companyRepository.findByCompanyId(id)
                .map(companyMapper::companyToResponseModel)
                .orElseThrow(() -> new ResourceNotFoundException("Company with id " + id + " does not exist"));
    }

    public List<Company> findCompanyByCompanyNameAdmin(String name) {
        return companyRepository.findAllCompany()
                .stream()
                .filter(c ->
                        c.getCompanyShortName().contains(name)
                                && c.getCompanyFullName().contains(name))
                .collect(Collectors.toList());
    }

    public List<CompanyResponseModel> findCompanyByCompanyName(String name) {
        return companyRepository.findAllCompany()
                .stream()
                .filter(c ->
                        c.getCompanyShortName().contains(name)
                                && c.getCompanyFullName().contains(name))
                .map(companyMapper::companyToResponseModel)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateCompanyByAdmin(Integer id, CompanyRequestModel requestModel, Principal connectedUser) {
        // tim company theo id
        Company updateCompany = findCompanyById(id);
        updateCompany(updateCompany, requestModel, userService.getCurrentUser(connectedUser).getUserId());
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
//        updateCompany.setCreatedAt(oldCompany.getCreatedAt());
//        updateCompany.setCreatedBy(oldCompany.getCreatedBy());
//        updateCompany.setUpdatedAt(Instant.now());
//        updateCompany.setUpdatedBy(updatedBy);
        companyRepository.save(updateCompany);
    }

    public void deleteCompany(Integer id) {
        Company company = findCompanyById(id);
        company.setDeleteFlag(true);
//        company.setUpdatedAt(Instant.now());
//        company.setUpdatedBy(userService.getCurrentUser(connectedUser).getUserId());
        companyRepository.save(company);
    }

    public List<CompanyResponseModel> getTopCompanies(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

        Page<Company> pagedResult = companyRepository.findAll(paging);

        List<CompanyResponseModel> list = pagedResult.getContent()
                .stream()
                .filter(u -> !u.isDeleteFlag())
                .map(companyMapper::companyToResponseModel)
                .collect(Collectors.toList());

        if (pagedResult.hasContent()) {
            return list;
        } else {
            return new ArrayList<>();
        }
    }

    public MyPagination<CompanyResponseModel> getAllCompanies(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

        Page<Company> pagedResult = companyRepository.findAll(paging);

        List<CompanyResponseModel> list = pagedResult.getContent()
                .stream()
                .filter(u -> !u.isDeleteFlag())
                .map(companyMapper::companyToResponseModel)
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

    public CompanyResponseModel findCompanyDisplayModel(Principal connectedUser) {
        Account account = userService.getCurrentAccount(connectedUser);
        return companyMapper.companyToResponseModel(findCompanyByEmail(account.getEmail()));
    }
}
