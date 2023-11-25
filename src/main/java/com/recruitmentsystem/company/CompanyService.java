package com.recruitmentsystem.company;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recruitmentsystem.account.Account;
import com.recruitmentsystem.account.AccountRepository;
import com.recruitmentsystem.account.AccountService;
import com.recruitmentsystem.address.address.Address;
import com.recruitmentsystem.address.address.AddressService;
import com.recruitmentsystem.common.exception.ResourceAlreadyExistsException;
import com.recruitmentsystem.common.exception.ResourceNotFoundException;
import com.recruitmentsystem.pagination.MyPagination;
import com.recruitmentsystem.role.RoleService;
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
    private final AccountService accountService;
    private final AddressService addressService;
    private final CompanyRepository companyRepository;
    private final RoleService roleService;
    private final UserService userService;
    private final CompanyMapper companyMapper;
    private final ObjectMapper objectMapper;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private void checkDuplicateCompanyName(String shortName, String fullName) {
        if (companyRepository.existsCompanyByCompanyShortName(shortName)) {
            throw new ResourceAlreadyExistsException("Company short name already taken");
        }
        if (companyRepository.existsCompanyByCompanyFullName(fullName)) {
            throw new ResourceAlreadyExistsException("Company full name already taken");
        }
    }

    public CompanyResponseModel addCompanyByAdmin(CompanyRequestModel request) {
        accountService.checkDuplicateEmail(request.email());
        checkDuplicateCompanyName(request.companyShortName(), request.companyFullName());

        Account account = Account.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(roleService.findRoleByName(request.roleName()))
                .enabled(true)
                .build();

        Company company = companyMapper.companyRequestModelToCompany(request);
//        Address address = addressService.addressRequestModelToEntity(request.companyAddress());

        accountRepository.save(account);
//        addressRepository.save(address);

        Address address = addressService.saveAddress(request.companyAddress());

        company.setAccount(account);
        company.setAddress(address);
        companyRepository.save(company);

        // bị lỗi trường lastModified và lastModifiedBy
        return companyMapper.companyToResponseModel(company);
    }

    public void existsById(Integer id) {
        if (!companyRepository.existsById(id)) {
            throw new ResourceNotFoundException("Company with id " + id + " not found");
        }
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

    public CompanyResponseModel findCompanyResponseModelById(Integer id) {
        return companyMapper.companyToResponseModel(findCompanyById(id));
    }

    public List<CompanyResponseModel> findCompanyByCompanyName(String name) {
        List<Company> companies = new ArrayList<>();
        companies.addAll(companyRepository.findByCompanyShortNameContainsIgnoreCase(name));
        companies.addAll(companyRepository.findByCompanyFullNameContainsIgnoreCase(name));
        return companies
                .stream()
                .map(companyMapper::companyToResponseModel)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateCompanyByAdmin(Integer id, CompanyRequestModel requestModel) {
        Company updateCompany = findCompanyById(id);
        updateCompany(updateCompany, requestModel);
    }

    public void updateCompanyByCompany(CompanyRequestModel request, Principal connectedUser) {
        Account account = userService.getCurrentAccount(connectedUser);
        Company company = findCompanyByEmail(account.getEmail());
        updateCompany(company, request);
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
        Address updateAddress = oldCompany.getAddress();
//        Address address = addressMapper.addressRequestModelToAddress(request.companyAddress());
//        updateAddress.setAddress(address.getAddress());
//        updateAddress.setWard(address.getWard());
//        updateAddress.setFullAddress(address.getFullAddress());
//        addressRepository.save(updateAddress);
        addressService.updateAddress(updateAddress.getAddressId(), request.companyAddress());

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

        Page<Company> pagedResult = companyRepository.findAllCompany(paging);

        if (pagedResult.hasContent()) {
            return pagedResult.getContent().stream()
                    .map(companyMapper::companyToResponseModel)
                    .toList();
        } else {
            return new ArrayList<>();
        }
    }

    public CompanyResponseModel findCompanyDisplayModel(Principal connectedUser) {
        Account account = userService.getCurrentAccount(connectedUser);
        return companyMapper.companyToResponseModel(findCompanyByEmail(account.getEmail()));
    }
}
