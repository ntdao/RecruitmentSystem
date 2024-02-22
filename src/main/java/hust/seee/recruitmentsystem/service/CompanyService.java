package hust.seee.recruitmentsystem.service;

import hust.seee.recruitmentsystem.dto.AddressDTO;
import hust.seee.recruitmentsystem.dto.CompanyDTO;
import hust.seee.recruitmentsystem.dto.StatisticDTO;
import hust.seee.recruitmentsystem.entity.Account;
import hust.seee.recruitmentsystem.entity.Address;
import hust.seee.recruitmentsystem.entity.Company;
import hust.seee.recruitmentsystem.exception.ResourceAlreadyExistsException;
import hust.seee.recruitmentsystem.exception.ResourceNotFoundException;
import hust.seee.recruitmentsystem.mapper.CompanyMapper;
import hust.seee.recruitmentsystem.repository.AccountRepository;
import hust.seee.recruitmentsystem.repository.CompanyRepository;
import hust.seee.recruitmentsystem.utils.DataFormat;
import hust.seee.recruitmentsystem.utils.Utils;
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
    public CompanyDTO addCompanyByAdmin(CompanyDTO request) {
        accountService.checkDuplicateEmail(request.getEmail());
        checkDuplicateCompanyName(request.getCompanyIndustryId(), request.getCompanyShortName(), request.getCompanyFullName());

        Account account = Account.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(roleService.findByName("COMPANY"))
                .enabled(true)
                .build();

        Company company = companyMapper.dtoToEntity(request);
        Address address = addressService.saveAddress(request.getCompanyAddress());
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

    public List<CompanyDTO> findAllCompanies() {
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

    public CompanyDTO findCompanyByCompanyId(Integer id) {
        return companyMapper.entityToDto(findCompanyById(id));
    }

    public Company findCompanyByEmail(String email) {
        return companyRepository.findCompanyByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Company with email " + email + " does not exist"));
    }

    public CompanyDTO findCompanyDTOById(Integer id) {
        return companyMapper.entityToDto(findCompanyById(id));
    }

    public List<CompanyDTO> findCompanyByCompanyName(String name) {
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

//    public void updateCompanyByCompany(CompanyDTO request, Principal connectedAccount) {
//        Company company = getCurrentCompany(connectedAccount);
//        updateCompanyInfo(company, request);
//    }
//
//    @Transactional
//    public void updateCompanyInfo(Company updateCompany, CompanyDTO request) {
//        int id = updateCompany.getCompanyId();
//        Account updateAccount = updateCompany.getAccount();
//
////        System.out.println(request.email());
//
//        boolean isEmailChange = !(request.getEmail() == null || updateAccount.getEmail().equals(request.getEmail()));
//        if (isEmailChange) {
//            accountService.checkDuplicateEmail(request.getEmail());
//            Account oldAccount = new Account(updateAccount, true);
//            System.out.println("Account - before update: " + oldAccount);
////            accountRepository.save(oldAccount);
//
//            updateAccount.setEmail(request.getEmail());
////            // đợi admin duyệt -> enable: true
////            updateAccount.setEnabled(false);
//            System.out.println("Account - after update: " + updateAccount);
//            accountRepository.save(updateAccount);
//        }
//
//        checkDuplicateCompanyName(request.getCompanyId(), request.getCompanyFullName(), request.getCompanyShortName());
//
//        // tao ban ghi luu thong tin cu cua company
//        Company oldCompany = new Company(updateCompany, true);
////        companyRepository.save(oldCompany);
//        System.out.println("Company - Old info: " + oldCompany);
//
//        // update company
//        Integer addressId = oldCompany.getCompanyAddress().getAddressId();
//        Address address = oldCompany.getCompanyAddress();
//        if (request.getCompanyAddress() != null) {
//            address = addressService.updateAddress(addressId, request.getCompanyAddress());
//        } else {
//            updateCompany = companyMapper.dtoToEntity(request);
//        }
//        updateCompany.setCompanyId(id);
//        updateCompany.setAccount(updateAccount);
//        updateCompany.setCompanyAddress(address);
////        // đợi admin duyệt -> enable: true
////        updateCompany.setEnable(false);
//
//        companyRepository.save(updateCompany);
//        System.out.println("Company - New info: " + updateCompany);
//    }

    @Transactional
    public void updateBasicInfoByCompany(CompanyDTO request, Principal principal) {
        Company updateCompany = getCurrentCompany(principal);

        boolean isNameChange = !updateCompany.getCompanyFullName().equals(request.getCompanyFullName()) &&
                updateCompany.getCompanyShortName().equals(request.getCompanyShortName());
        if (isNameChange) {
            checkDuplicateCompanyName(request.getCompanyId(), request.getCompanyFullName(), request.getCompanyShortName());
        }

//        // tao ban ghi luu thong tin cu cua company
//        Company oldCompany = new Company(updateCompany, true);
//        companyRepository.save(oldCompany);
        System.out.println("Company - Old info: " + updateCompany);

        // update company
        updateCompany.setCompanyFullName(request.getCompanyFullName());
        updateCompany.setCompanyShortName(request.getCompanyShortName());
        updateCompany.setIndustry(industryService.findById(request.getCompanyIndustryId()));
        updateCompany.setCompanyLogo(request.getCompanyLogo());
        updateCompany.setCompanySize(request.getCompanySize());
        updateCompany.setCompanyFoundedYear(request.getCompanyFoundedYear());
        updateCompany.setCompanyMst(request.getCompanyMST());
        updateCompany.setCompanyLicense(request.getCompanyLicense());

        companyRepository.save(updateCompany);
        System.out.println("Company - New info: " + updateCompany);
    }

    @Transactional
    public void updateCompanyAddressByCompany(AddressDTO addressRequestModel, Principal connectedUser) {
        Company company = getCurrentCompany(connectedUser);
        Integer addressId = company.getCompanyAddress().getAddressId();
        addressService.updateAddress(addressId, addressRequestModel);
        company.setLastModified(LocalDateTime.now());
        companyRepository.save(company);
    }

    @Transactional
    public void updateContactByCompany(CompanyDTO request, Principal connectedUser) {
        Company company = getCurrentCompany(connectedUser);
        Account updateAccount = company.getAccount();

        if (!company.getAccount().getEmail().equals(request.getEmail())) {
            accountService.checkDuplicateEmail(request.getEmail());
//            Account oldAccount = new Account(updateAccount, true);
            System.out.println("Account - before update: " + updateAccount);
//            accountRepository.save(oldAccount);

            updateAccount.setEmail(request.getEmail());
            System.out.println("Account - after update: " + updateAccount);
            accountRepository.save(updateAccount);
        }

        // tao ban ghi luu thong tin cu cua company
//        Company oldCompany = new Company(company, true);
        System.out.println("Company - Old info: " + company);

        // update company
        company.setPhoneNumber(request.getPhoneNumber());
        company.setWebsite(request.getWebsite());
        company.setFacebookUrl(request.getFacebookUrl());
        company.setYoutubeUrl(request.getYoutubeUrl());
        company.setLinkedinUrl(request.getLinkedinUrl());
        companyRepository.save(company);
        System.out.println("Company - New info: " + company);
    }

    @Transactional
    public void updateDescByCompany(CompanyDTO request, Principal connectedUser) {
        Company company = getCurrentCompany(connectedUser);
        System.out.println("Company - Old info: " + company);

        company.setCompanyIntroduction(request.getCompanyIntroduction());
        company.setCompanyImage(request.getCompanyImage());
//        company.setCompanyId(company.getCompanyId());
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

    public List<CompanyDTO> getTopCompanies(Integer pageNo, Integer pageSize, String sortBy) {
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

    public CompanyDTO findCompanyDisplayModel(Principal connectedAccount) {
        Account account = accountService.getCurrentAccount(connectedAccount);
        return companyMapper.entityToDto(findCompanyByEmail(account.getEmail()));
    }

    public String uploadCompanyImage(Principal connectedUser, MultipartFile file, String field) {
        Company company = getCurrentCompany(connectedUser);
        return prefix + s3Service.uploadFile("%s/%s/".formatted(field, company.getCompanyId()), file);
    }

    public StatisticDTO getQuantity() {
        List<Map<String, Object>> map = companyRepository.getQuantity();
        return Utils.getStatistic(map);
    }

    public Page<CompanyDTO> getCompanyPaging(CompanyDTO dto) {
        Pageable pageable = PageRequest.of(dto.getPage() - 1, dto.getSize());
        return companyRepository.findAll(
                        DataFormat.lower(dto.getCompanyFullName()),
                        dto.getCompanyIndustryId(),
                        dto.getProvinceCode(),
                        pageable)
                .map(companyMapper::entityToDto);
    }
}
