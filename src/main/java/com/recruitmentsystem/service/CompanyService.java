package com.recruitmentsystem.service;

import com.recruitmentsystem.dto.*;
import com.recruitmentsystem.entity.Account;
import com.recruitmentsystem.entity.Address;
import com.recruitmentsystem.entity.Company;
import com.recruitmentsystem.entity.Job;
import com.recruitmentsystem.exception.ResourceAlreadyExistsException;
import com.recruitmentsystem.exception.ResourceNotFoundException;
import com.recruitmentsystem.mapper.CompanyMapper;
import com.recruitmentsystem.repository.AccountRepository;
import com.recruitmentsystem.repository.CompanyRepository;
import com.recruitmentsystem.utils.DataFormat;
import com.recruitmentsystem.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final AccountRepository accountRepository;
    private final AccountService accountService;
    private final AddressService addressService;
    private final CompanyMapper companyMapper;
    private final CompanyRepository companyRepository;
    private final IndustryService industryService;
    private final RoleService roleService;
    private final S3Service s3Service;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Value("${aws.s3.prefix}")
    private String prefix;

    public Company getCurrentCompany(Principal connectedAccount) {
        return findCompanyByEmail(accountService.getCurrentAccount(connectedAccount).getEmail());
    }

    private void checkDuplicateCompanyName(Integer id, String shortName, String fullName) {
        if (companyRepository.countByName(id, shortName, fullName) > 0) {
            throw new ResourceAlreadyExistsException("Tên công ty đã tồn tại");
        }
    }

    @Transactional
    public CompanyResponseModel addCompanyByAdmin(CompanyDto request) {
        accountService.checkDuplicateEmail(request.email());
        checkDuplicateCompanyName(request.companyId(), request.companyShortName(), request.companyFullName());

        Account account = Account.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(roleService.findByName("COMPANY"))
                .enabled(true)
                .build();

        Company company = companyMapper.dtoToEntity(request);
        Address address = addressService.saveAddress(request.companyAddress());
        accountRepository.save(account);

        company.setAccount(account);
        company.setCompanyAddress(address);
        companyRepository.save(company);

        // bị lỗi trường lastModified và lastModifiedBy
        return companyMapper.entityToDto(company);
    }

    public void existsById(Integer id) {
        if (!companyRepository.existsById(id)) {
            throw new ResourceNotFoundException("Company with id " + id + " not found");
        }
    }

    public List<CompanyResponseModel> findAllCompanies() {
        List<Company> companies = companyRepository.findAllCompany();
        return companies.stream()
                .map(companyMapper::entityToDto)
                .collect(Collectors.toList());
    }

    public List<Company> findAllCompaniesAdmin() {
        return companyRepository.findAllCompany();
    }

    public Company findCompanyById(Integer id) {
        return companyRepository.findByCompanyId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company with id " + id + " does not exist"));
    }

    public CompanyResponseModel findCompanyByCompanyId(Integer id) {
        return companyMapper.entityToDto(findCompanyById(id));
    }

    public Company findCompanyByEmail(String email) {
        return companyRepository.findCompanyByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Company with email " + email + " does not exist"));
    }

    public CompanyResponseModel findCompanyResponseModelById(Integer id) {
        return companyMapper.entityToDto(findCompanyById(id));
    }

    public List<CompanyResponseModel> findCompanyByCompanyName(String name) {
        return companyRepository.findByName(name)
                .stream()
                .map(companyMapper::entityToDto)
                .collect(Collectors.toList());
    }

//    @Transactional
//    public void updateCompanyByAdmin(CompanyDto dto) {
//        Company updateCompany = findCompanyById(dto.companyId());
//        updateCompany(updateCompany, dto);
//    }

    public void updateCompanyByCompany(CompanyDto request, Principal connectedAccount) {
        Company company = getCurrentCompany(connectedAccount);
        updateCompanyInfo(company, request);
    }

    @Transactional
    public void updateCompanyInfo(Company updateCompany, CompanyDto request) {
        int id = updateCompany.getCompanyId();
        Account updateAccount = updateCompany.getAccount();

        System.out.println(request.email());

        boolean isEmailChange = !(request.email() == null || updateAccount.getEmail().equals(request.email()));
        if (isEmailChange) {
            accountService.checkDuplicateEmail(request.email());
            Account oldAccount = new Account(updateAccount, true);
            System.out.println("Account - before update: " + oldAccount);
            accountRepository.save(oldAccount);

            updateAccount.setEmail(request.email());
//            // đợi admin duyệt -> enable: true
//            updateAccount.setEnabled(false);
            System.out.println("Account - after update: " + updateAccount);
            accountRepository.save(updateAccount);
        }

        checkDuplicateCompanyName(request.companyId(), request.companyFullName(), request.companyShortName());

        // tao ban ghi luu thong tin cu cua company
        Company oldCompany = new Company(updateCompany, true);
        companyRepository.save(oldCompany);
        System.out.println("Company - Old info: " + oldCompany);

        // update company
        Integer addressId = oldCompany.getCompanyAddress().getAddressId();
        Address address = oldCompany.getCompanyAddress();
        if (request.companyAddress() != null) {
            address = addressService.updateAddress(addressId, request.companyAddress());
        } else {
            updateCompany = companyMapper.dtoToEntity(request);
        }
        updateCompany.setCompanyId(id);
        updateCompany.setAccount(updateAccount);
        updateCompany.setCompanyAddress(address);
//        // đợi admin duyệt -> enable: true
//        updateCompany.setEnable(false);

        companyRepository.save(updateCompany);
        System.out.println("Company - New info: " + updateCompany);
    }

    @Transactional
    public void updateBasicInfoByCompany(CompanyDto request, Principal principal) {
        Company updateCompany = getCurrentCompany(principal);

        boolean isNameChange = !updateCompany.getCompanyFullName().equals(request.companyFullName()) &&
                updateCompany.getCompanyShortName().equals(request.companyShortName());
        if (isNameChange) {
            checkDuplicateCompanyName(request.companyId(), request.companyFullName(), request.companyShortName());
        }

        // tao ban ghi luu thong tin cu cua company
        Company oldCompany = new Company(updateCompany, true);
        companyRepository.save(oldCompany);
        System.out.println("Company - Old info: " + updateCompany);

        // update company
        updateCompany.setCompanyFullName(request.companyFullName());
        updateCompany.setCompanyShortName(request.companyShortName());
        updateCompany.setIndustry(industryService.findById(request.companyIndustryId()));
        updateCompany.setCompanyLogo(request.companyLogo());
        updateCompany.setCompanySize(request.companySize());
        updateCompany.setCompanyFoundedYear(request.companyFoundedYear());
        updateCompany.setCompanyMst(request.companyMST());
        updateCompany.setCompanyLicense(request.companyLicense());

        companyRepository.save(updateCompany);
        System.out.println("Company - New info: " + updateCompany);
    }

    @Transactional
    public void updateCompanyAddressByCompany(AddressDto addressRequestModel, Principal connectedUser) {
        Company company = getCurrentCompany(connectedUser);
        Integer addressId = company.getCompanyAddress().getAddressId();
        addressService.updateAddress(addressId, addressRequestModel);
        company.setLastModified(LocalDateTime.now());
        companyRepository.save(company);
    }

    @Transactional
    public void updateContactByCompany(CompanyDto request, Principal connectedUser) {
        Company company = getCurrentCompany(connectedUser);
        Account updateAccount = company.getAccount();

        if (!company.getAccount().getEmail().equals(request.email())) {
            accountService.checkDuplicateEmail(request.email());
            Account oldAccount = new Account(updateAccount, true);
            System.out.println("Account - before update: " + oldAccount);
            accountRepository.save(oldAccount);

            updateAccount.setEmail(request.email());
            System.out.println("Account - after update: " + updateAccount);
            accountRepository.save(updateAccount);
        }

        // tao ban ghi luu thong tin cu cua company
        Company oldCompany = new Company(company, true);
        System.out.println("Company - Old info: " + oldCompany);

        // update company
        company.setPhoneNumber(request.phoneNumber());
        company.setWebsite(request.website());
        company.setFacebookUrl(request.facebookUrl());
        company.setYoutubeUrl(request.youtubeUrl());
        company.setLinkedinUrl(request.linkedinUrl());
        company.setCompanyId(oldCompany.getCompanyId());
        companyRepository.save(company);
        System.out.println("Company - New info: " + company);
    }

    @Transactional
    public void updateDescByCompany(CompanyDto request, Principal connectedUser) {
        Company company = getCurrentCompany(connectedUser);
        System.out.println("Company - Old info: " + company);

        company.setCompanyIntroduction(request.companyIntroduction());
        company.setCompanyImage(request.companyImage());
        company.setCompanyId(company.getCompanyId());
        companyRepository.save(company);
        System.out.println("Company - New info: " + company);
    }

    @Transactional
    public void deleteCompany(Integer id) {
        Company company = findCompanyById(id);
        company.setDeleteFlag(true);
        Account account = company.getAccount();
        account.setDeleteFlag(true);

        accountRepository.save(account);
        companyRepository.save(company);

        accountService.revokeAllAccountTokens(account.getId());
    }

    public List<CompanyResponseModel> getTopCompanies(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

        Page<Company> pagedResult = companyRepository.findAllCompany(paging);

        if (pagedResult.hasContent()) {
            return pagedResult.getContent().stream()
                    .map(companyMapper::entityToDto)
                    .toList();
        } else {
            return new ArrayList<>();
        }
    }

    public List<Map<String, Object>> getTopCompaniesModel(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

        Page<Map<String, Object>> pagedResult = companyRepository.findTopCompany(paging);

        if (pagedResult.hasContent()) {
            return pagedResult.getContent();
        } else {
            return new ArrayList<>();
        }
    }

    public CompanyResponseModel findCompanyDisplayModel(Principal connectedAccount) {
        Account account = accountService.getCurrentAccount(connectedAccount);
        return companyMapper.entityToDto(findCompanyByEmail(account.getEmail()));
    }

    public String uploadCompanyImage(Principal connectedUser, MultipartFile file, String field) {
        Company company = getCurrentCompany(connectedUser);
        return prefix + s3Service.uploadFile("%s/%s/".formatted(field, company.getCompanyId()), file);
    }

    public StatisticDto getQuantity() {
        List<Map<String, Object>> map = companyRepository.getQuantity();
        return Utils.getStatistic(map);
    }

    public List<CompanyResponseModel> getCompanyPaging(CompanyDto dto) {
        List<Company> companies = companyRepository.findAll(
                DataFormat.lower(dto.companyFullName()),
                dto.companyIndustryId(),
                dto.provinceCode()
        );
        return companies.stream().map(companyMapper::entityToDto).toList();
    }
}
