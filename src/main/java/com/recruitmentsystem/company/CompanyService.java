package com.recruitmentsystem.company;

import com.recruitmentsystem.account.Account;
import com.recruitmentsystem.account.AccountRepository;
import com.recruitmentsystem.account.AccountService;
import com.recruitmentsystem.common.exception.ResourceAlreadyExistsException;
import com.recruitmentsystem.common.exception.ResourceNotFoundException;
import com.recruitmentsystem.pagination.MyPagination;
import com.recruitmentsystem.role.RoleService;
import com.recruitmentsystem.user.User;
import com.recruitmentsystem.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final AccountRepository accountRepository;
    private final CompanyRepository companyRepository;
    private final AccountService accountService;
    private final RoleService roleService;
    private final UserService userService;
    private final CompanyMapper companyMapper;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private void checkDuplicateCompanyName(String shortName, String fullName) {
        if (companyRepository.existsCompanyByCompanyShortName(shortName)) {
            throw new ResourceAlreadyExistsException("Company short name already taken");
        }
        if (companyRepository.existsCompanyByCompanyFullName(fullName)) {
            throw new ResourceAlreadyExistsException("Company full name already taken");
        }
    }

    public CompanyResponseModel addCompanyAdmin(CompanyRequestModel request, Principal connectedUser) {
        accountService.checkDuplicateEmail(request.email());
        checkDuplicateCompanyName(request.companyShortName(), request.companyFullName());

        Account account = Account.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(roleService.findRoleByName(request.roleName()))
                .enabled(true)
                .build();

        Company company = companyMapper.companyRequestModelToCompany(request);

        accountRepository.save(account);
        company.setAccount(account);
        companyRepository.save(company);

        // bị lỗi trường lastModified và lastModifiedBy
        return companyMapper.companyToResponseModel(company);
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
        updateCompany(updateCompany, requestModel);
    }
    public CompanyResponseModel updateCompanyByCompany(CompanyRequestModel request, Principal connectedUser) {
        Account account = userService.getCurrentAccount(connectedUser);
        Company company = findCompanyByEmail(account.getEmail());
        return null;
    }

    private void updateCompany(Company updateCompany, CompanyRequestModel request) {
        int id = updateCompany.getCompanyId();
        Account updateAccount = updateCompany.getAccount();

        boolean isEmailChange = !updateAccount.getEmail().equals(request.email());
        if (isEmailChange) {
            accountService.checkDuplicateEmail(request.email());
        }

        boolean isNameChange = !updateCompany.getCompanyFullName().equals(request.companyFullName()) &&
                updateCompany.getCompanyShortName().equals(request.companyShortName());
        if (isNameChange) {
            checkDuplicateCompanyName(request.companyFullName(), request.companyShortName());
        }

        Account oldAccount = new Account(updateAccount, true);
        System.out.println("Account - before update: " + oldAccount);
        accountRepository.save(oldAccount);

        updateAccount.setEmail(request.email());
        System.out.println("Account - after update: " + updateAccount);
        accountRepository.save(updateAccount);

        // tao ban ghi luu thong tin cu cua company
        Company oldCompany = new Company(updateCompany, true);
        companyRepository.save(oldCompany);
        System.out.println("Company - Old info: " + oldCompany);

        // update company
        updateCompany = companyMapper.companyRequestModelToCompany(request);
        updateCompany.setCompanyId(id);
        updateCompany.setAccount(updateAccount);
        companyRepository.save(updateCompany);
        System.out.println("Company - New info: " + updateCompany);
    }

    public void deleteCompany(Integer id) {
        Company company = findCompanyById(id);
        company.setDeleteFlag(true);
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
