package com.recruitmentsystem.service;

import com.recruitmentsystem.dto.AddressDto;
import com.recruitmentsystem.dto.CompanyRequestModel;
import com.recruitmentsystem.dto.CompanyResponseModel;
import com.recruitmentsystem.dto.CompanyTopModel;
import com.recruitmentsystem.entity.Account;
import com.recruitmentsystem.entity.Address;
import com.recruitmentsystem.entity.Company;
import com.recruitmentsystem.exception.ResourceAlreadyExistsException;
import com.recruitmentsystem.exception.ResourceNotFoundException;
import com.recruitmentsystem.mapper.CompanyMapper;
import com.recruitmentsystem.repository.AccountRepository;
import com.recruitmentsystem.repository.CompanyRepository;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
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
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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

    public Company getCurrentCompany(Principal connectedAccount) {
        return findCompanyByEmail(accountService.getCurrentAccount(connectedAccount).getEmail());
    }

    private void checkDuplicateCompanyName(String shortName, String fullName) {
        if (companyRepository.existsCompanyByCompanyShortName(shortName)) {
            throw new ResourceAlreadyExistsException("Company short name already taken");
        }
        if (companyRepository.existsCompanyByCompanyFullName(fullName)) {
            throw new ResourceAlreadyExistsException("Company full name already taken");
        }
    }

    @Transactional
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
        Address address = addressService.saveAddress(request.companyAddress());
        accountRepository.save(account);

        company.setAccount(account);
        company.setCompanyAddress(address);
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
        System.out.println(companies);
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
        System.out.println(name);
        return companyRepository.findByName(name)
                .stream()
                .map(companyMapper::companyToResponseModel)
                .collect(Collectors.toList());
    }

//    @Transactional
//    public void updateCompanyByAdmin(Integer id, CompanyRequestModel requestModel) {
//        Company updateCompany = findCompanyById(id);
//        updateCompany(updateCompany, requestModel);
//    }

    public void updateCompanyByCompany(CompanyRequestModel request, Principal connectedAccount) {
        Company company = getCurrentCompany(connectedAccount);
        updateCompanyInfo(company, request);
    }

    @Transactional
    public void updateCompanyInfo(Company updateCompany, CompanyRequestModel request) {
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

        boolean isNameChange = !updateCompany.getCompanyFullName().equals(request.companyFullName()) &&
                updateCompany.getCompanyShortName().equals(request.companyShortName());
        if (isNameChange) {
            checkDuplicateCompanyName(request.companyFullName(), request.companyShortName());
        }

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
            updateCompany = companyMapper.companyRequestModelToCompany(request);
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
    public void updateBasicInfoByCompany(CompanyRequestModel request, Principal principal) {
        Company updateCompany = getCurrentCompany(principal);

        boolean isNameChange = !updateCompany.getCompanyFullName().equals(request.companyFullName()) &&
                updateCompany.getCompanyShortName().equals(request.companyShortName());
        if (isNameChange) {
            checkDuplicateCompanyName(request.companyFullName(), request.companyShortName());
        }

        // tao ban ghi luu thong tin cu cua company
        Company oldCompany = new Company(updateCompany, true);
        companyRepository.save(oldCompany);
        System.out.println("Company - Old info: " + updateCompany);

        // update company
        updateCompany.setCompanyFullName(request.companyFullName());
        updateCompany.setCompanyShortName(request.companyShortName());
        updateCompany.setIndustry(industryService.findById(Integer.parseInt(request.companyIndustry())));
        updateCompany.setCompanyLogo(request.companyLogo());
        updateCompany.setCompanySize(request.companySize());
        updateCompany.setCompanyFoundedYear(Integer.parseInt(request.companyFoundedYear()));
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
    public void updateContactByCompany(CompanyRequestModel request, Principal connectedUser) {
        Company company = getCurrentCompany(connectedUser);
        Account updateAccount = company.getAccount();

        if (!company.getAccount().getEmail().equals(request.email())) {
            accountService.checkDuplicateEmail(request.email());
        }
        Account oldAccount = new Account(updateAccount, true);
        System.out.println("Account - before update: " + oldAccount);
        accountRepository.save(oldAccount);

        updateAccount.setEmail(request.email());
        System.out.println("Account - after update: " + updateAccount);
        accountRepository.save(updateAccount);

        // tao ban ghi luu thong tin cu cua company
        Company oldCompany = new Company(company, true);
        System.out.println("Company - Old info: " + company);

        // update company
        company.setPhoneNumber(request.phoneNumber());
        company.setWebsite(request.website());
        company.setFacebookUrl(request.facebookUrl());
        company.setYoutubeUrl(request.youtubeUrl());
        company.setLinkedinUrl(request.linkedinUrl());

        companyRepository.save(company);
        System.out.println("Company - New info: " + company);
    }

    @Transactional
    public void updateDescByCompany(CompanyRequestModel request, Principal connectedUser) {
        Company company = getCurrentCompany(connectedUser);
        System.out.println("Company - Old info: " + company);

        company.setCompanyIntroduction(request.companyIntroduction());
        company.setCompanyImage(request.companyImage());

        companyRepository.save(company);
        System.out.println("Company - New info: " + company);
    }

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
                    .map(companyMapper::companyToResponseModel)
                    .toList();
        } else {
            return new ArrayList<>();
        }
    }

    public List<CompanyTopModel> getTopCompaniesModel(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

        Page<CompanyTopModel> pagedResult = companyRepository.findTopCompany(paging);

        if (pagedResult.hasContent()) {
            return pagedResult.getContent();
        } else {
            return new ArrayList<>();
        }
    }

    public CompanyResponseModel findCompanyDisplayModel(Principal connectedAccount) {
        Account account = accountService.getCurrentAccount(connectedAccount);
        return companyMapper.companyToResponseModel(findCompanyByEmail(account.getEmail()));
    }

    public String uploadCompanyImage(Principal connectedUser, MultipartFile[] files, String field) {
        Company company = getCurrentCompany(connectedUser);
        String image = "";
        for (MultipartFile file : files) {
            String imgUrl = s3Service.uploadFile("%s/%s/".formatted(field, company.getCompanyId()), file);
            image = imgUrl + ";";
            System.out.println(imgUrl);
        }
        System.out.println("Company image url: " + image);
        return image;
    }

    public String addImage() {
        return null;
    }

    public String removeImage() {
        return null;
    }

    public byte[] getCompanyImage(Integer companyId, String field) {
        Company company = findCompanyById(companyId);
        String fileName = "";
        switch (field) {
            case "company-logo":
                fileName = company.getCompanyLogo();
                break;
            case "company-images":
                fileName = company.getCompanyImage();
                break;
            case "company-license":
                fileName = company.getCompanyLicense();
                break;
            default:
        }
        if (StringUtils.isBlank(fileName)) {
            throw new ResourceNotFoundException("Company with id [%s] [%s] not found".formatted(companyId, field));
        }
        return s3Service.downloadFile(fileName);
    }

    public void deleteCompanyImage(String fileName) {
        s3Service.deleteFile(fileName);
    }

//    public void updateCompanyAddressByCompany(AddressDto addressRequestModel, Principal connectedUser) {
//        Company company = getCurrentCompany(connectedUser);
//        Integer addressId = company.getCompanyAddress().getAddressId();
//        addressService.updateAddress(addressId, addressRequestModel);
//        companyRepository.save(company);
//    }
//
//    @Transactional
//    public void updateContactByCompany(CompanyRequestModel request, Principal connectedUser) {
//        Company company = getCurrentCompany(connectedUser);
//        Account updateAccount = company.getAccount();
//
//        if (!company.getAccount().getEmail().equals(request.email())) {
//            accountService.checkDuplicateEmail(request.email());
//        }
//        Account oldAccount = new Account(updateAccount, true);
//        System.out.println("Account - before update: " + oldAccount);
//        accountRepository.save(oldAccount);
//
//        updateAccount.setEmail(request.email());
//        System.out.println("Account - after update: " + updateAccount);
//        accountRepository.save(updateAccount);
//
//        // tao ban ghi luu thong tin cu cua company
////        Company oldCompany = new Company(company, true);
//        System.out.println("Company - Old info: " + company);
//
//        // update company
//        company.setPhoneNumber(request.phoneNumber());
//        company.setWebsite(request.website());
//        company.setFacebookUrl(request.facebookUrl());
//        company.setYoutubeUrl(request.youtubeUrl());
//        company.setLinkedinUrl(request.linkedinUrl());
//
//        companyRepository.save(company);
//        System.out.println("Company - New info: " + company);
//    }
//
//    public void updateDescByCompany(CompanyRequestModel request, Principal connectedUser) {
//        Company company = getCurrentCompany(connectedUser);
//        System.out.println("Company - Old info: " + company);
//
//        company.setCompanyIntroduction(request.companyIntroduction());
//        company.setCompanyImage(request.companyImage());
//
//        companyRepository.save(company);
//        System.out.println("Company - New info: " + company);
//    }
}
