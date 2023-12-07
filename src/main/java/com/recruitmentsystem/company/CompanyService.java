package com.recruitmentsystem.company;

import com.recruitmentsystem.account.Account;
import com.recruitmentsystem.account.AccountRepository;
import com.recruitmentsystem.account.AccountService;
import com.recruitmentsystem.address.address.Address;
import com.recruitmentsystem.address.address.AddressRequestModel;
import com.recruitmentsystem.address.address.AddressService;
import com.recruitmentsystem.common.exception.ResourceAlreadyExistsException;
import com.recruitmentsystem.common.exception.ResourceNotFoundException;
import com.recruitmentsystem.industry.IndustryMapper;
import com.recruitmentsystem.industry.IndustryService;
import com.recruitmentsystem.role.RoleService;
import com.recruitmentsystem.s3.S3Service;
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
        String email = accountService.getCurrentAccount(connectedAccount).getEmail();
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

    @Transactional
    public void updateCompanyByCompany(CompanyRequestModel request, Principal connectedAccount) {
        Company company = getCurrentCompany(connectedAccount);
        updateCompanyBasicInfo(company, request);
    }

    private void updateCompanyBasicInfo(Company updateCompany, CompanyRequestModel request) {
//        int id = updateCompany.getCompanyId();
//        Account updateAccount = updateCompany.getAccount();

//        boolean isEmailChange = !updateAccount.getEmail().equals(request.email());
//        if (isEmailChange) {
//            accountService.checkDuplicateEmail(request.email());
//        }

        boolean isNameChange = !updateCompany.getCompanyFullName().equals(request.companyFullName()) &&
                updateCompany.getCompanyShortName().equals(request.companyShortName());
        if (isNameChange) {
            checkDuplicateCompanyName(request.companyFullName(), request.companyShortName());
        }

//        Account oldAccount = new Account(updateAccount, true);
//        System.out.println("Account - before update: " + oldAccount);
//        accountRepository.save(oldAccount);
//
//        updateAccount.setEmail(request.email());
//        System.out.println("Account - after update: " + updateAccount);
//        accountRepository.save(updateAccount);

        // tao ban ghi luu thong tin cu cua company
//        Company oldCompany = new Company(updateCompany, true);
//        companyRepository.save(oldCompany);
        System.out.println("Company - Old info: " + updateCompany);

        // update company
//        updateCompany = companyMapper.companyRequestModelToCompany(request);
//        System.out.println(updateCompany);
//        updateCompany.setCompanyId(oldCompany.getCompanyId());
//        updateCompany.setAccount(updateAccount);
//        updateCompany.setAddress(oldCompany.getAddress());

        updateCompany.setCompanyFullName(request.companyFullName());
        updateCompany.setCompanyShortName(request.companyShortName());
        updateCompany.setIndustry(industryService.findById(request.industryId()));
        updateCompany.setCompanyLogo(request.companyLogo());
        updateCompany.setCompanySize(request.companySize());
        updateCompany.setCompanyFoundedYear(request.companyFoundedYear());

        companyRepository.save(updateCompany);
        System.out.println("Company - New info: " + updateCompany);
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

    public String uploadCompanyLogo(Principal connectedUser, MultipartFile file) {
        Company company = getCurrentCompany(connectedUser);
        String image = s3Service.uploadFile("company-logo/%s/".formatted(company.getCompanyId()), file);
        System.out.println(image);
        return image;
    }

    public String uploadCompanyImage(Principal connectedUser, MultipartFile[] files) {
        Company company = getCurrentCompany(connectedUser);
        String image = "";
        for (MultipartFile file : files) {
            String imgUrl = s3Service.uploadFile("company-images/%s/".formatted(company.getCompanyId()), file);
            image = imgUrl + ";";
            System.out.println(imgUrl);
        }
        System.out.println("Company image url: " + image);
        return image;
    }

    public byte[] getCompanyImage(Integer companyId) {
        Company company = findCompanyById(companyId);

        if (StringUtils.isBlank(company.getCompanyImage())) {
            throw new ResourceNotFoundException("Company with id [%s] image not found".formatted(companyId));
        }

        return s3Service.downloadFile(company.getCompanyImage());
    }

    public void updateCompanyAddressByCompany(AddressRequestModel addressRequestModel, Principal connectedUser) {
        Company company = getCurrentCompany(connectedUser);
        Integer addressId = company.getAddress().getAddressId();
        addressService.updateAddress(addressId, addressRequestModel);
        companyRepository.save(company);
    }

    @Transactional
    public void updateContactByCompany(CompanyRequestModel request, Principal connectedUser) {
        Company company = getCurrentCompany(connectedUser);
        Account updateAccount = company.getAccount();

        if(! company.getAccount().getEmail().equals(request.email())) {
            accountService.checkDuplicateEmail(request.email());
        }
        Account oldAccount = new Account(updateAccount, true);
        System.out.println("Account - before update: " + oldAccount);
        accountRepository.save(oldAccount);

        updateAccount.setEmail(request.email());
        System.out.println("Account - after update: " + updateAccount);
        accountRepository.save(updateAccount);

        // tao ban ghi luu thong tin cu cua company
//        Company oldCompany = new Company(company, true);
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

    public void updateDescByCompany(CompanyRequestModel request, Principal connectedUser) {
        Company company = getCurrentCompany(connectedUser);
        System.out.println("Company - Old info: " + company);

        company.setCompanyIntroduction(request.companyIntroduction());
        company.setCompanyImage(request.companyImage());

        companyRepository.save(company);
        System.out.println("Company - New info: " + company);
    }
}
