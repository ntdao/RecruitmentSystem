package com.recruitmentsystem.service;

import com.recruitmentsystem.dto.*;
import com.recruitmentsystem.entity.Account;
import com.recruitmentsystem.entity.Candidate;
import com.recruitmentsystem.entity.CandidateEducation;
import com.recruitmentsystem.enums.TokenType;
import com.recruitmentsystem.exception.InputException;
import com.recruitmentsystem.exception.ResourceNotFoundException;
import com.recruitmentsystem.mapper.CandidateMapper;
import com.recruitmentsystem.pagination.PageDto;
import com.recruitmentsystem.repository.AccountRepository;
import com.recruitmentsystem.repository.CandidateRepository;
import com.recruitmentsystem.security.jwt.JwtService;
import com.recruitmentsystem.security.token.Token;
import com.recruitmentsystem.security.token.TokenService;
import io.micrometer.common.util.StringUtils;
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
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CandidateService {
    private final AccountRepository accountRepository;
    private final AccountService accountService;
    private final AddressService addressService;
    private final CandidateRepository candidateRepository;
    private final CandidateMapper candidateMapper;
    private final CandidateEducationService candidateEducationService;
    private final FileService fileService;
    private final JwtService jwtService;
    private final RoleService roleService;
    private final S3Service s3Service;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Value("${aws.s3.prefix}")
    private String prefix;
    @Value("${app.image.root}")
    private String root;

    @Transactional
    public CandidateResponseModel addCandidate(CandidateRequestModel request) {
        accountService.checkDuplicateEmail(request.email());
        Account account = Account.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(roleService.findRoleByName(request.roleName()))
                .enabled(true)
                .build();
        accountRepository.save(account);

        addressService.saveAddress(request.address());

        Candidate candidate = candidateMapper.dtoToEntity(request);
        candidate.setAccount(account);
        candidateRepository.save(candidate);

        // bị lỗi trường lastModified và lastModifiedBy
        return candidateMapper.entityToDto(candidate);
    }

    public List<CandidateResponseModel> findAllCandidates() {
        return candidateRepository.findAllCandidate().stream()
                .map(candidateMapper::entityToDto).collect(Collectors.toList());
    }

    public Page<CandidateResponseModel> searchCandidate(PageDto pageDto) {
        Pageable paging = PageRequest.of(
                pageDto.getPageNo(),
                pageDto.getPageSize(),
                Sort.Direction.fromString(pageDto.getSortDir()),
                pageDto.getSortBy());
        Page<Candidate> pagedResult = candidateRepository.findAllCandidate(paging);
        return pagedResult.map(candidateMapper::entityToDto);
    }

    public CandidateResponseModel findCandidateResponseModelById(Integer id) {
        return candidateMapper.entityToDto(findCandidateById(id));
    }

    private Candidate findCandidateById(Integer id) {
        return candidateRepository.findCandidateById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate with id " + id + " does not exist"));
    }

    private List<Candidate> findListCandidateByIds(List<Integer> ids) {
        return candidateRepository.findListCandidateByIds(ids);
    }

    public List<CandidateResponseModel> findCandidatesByIds(List<Integer> ids) {
        return findListCandidateByIds(ids)
                .stream()
                .map(candidateMapper::entityToDto)
                .toList();
    }

    public List<CandidateResponseModel> findAllCandidateByName(String name) {
        return candidateRepository.findAllCandidateByName(name)
                .stream()
                .map(candidateMapper::entityToDto)
                .collect(Collectors.toList());
    }

    public Candidate findCandidateByEmail(String email) {
        return candidateRepository.findCandidateByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate with email " + email + " does not exist"));
    }

    @Transactional
    public AuthenticationResponseModel updateCandidateByCandidate(CandidateRequestModel request, Principal connectedCandidate) {
        Candidate updateCandidate = getCurrentCandidate(connectedCandidate);
        return updateCandidate(updateCandidate, request);
    }

    private AuthenticationResponseModel updateCandidate(Candidate updateCandidate, CandidateRequestModel request) {
        int candidateId = updateCandidate.getCandidateId();
        Account updateAccount = updateCandidate.getAccount();
        int accountId = updateAccount.getId();

        boolean isEmailChange = !updateCandidate.getAccount().getEmail().equals(request.email());
        if (isEmailChange) {
            accountService.checkDuplicateEmail(request.email());
            Account oldAccount = new Account(updateAccount, true);
            System.out.println("Account - before update: " + oldAccount);
            accountRepository.save(oldAccount);

            updateAccount.setEmail(request.email());
            System.out.println("Account - after update: " + updateAccount);
            accountRepository.save(updateAccount);
        }

        // tao ban ghi luu thong tin cu cua candidate
        Candidate oldCandidate = new Candidate(updateCandidate, true);
//        oldCandidate.setAccount(updateAccount);
        System.out.println("Candidate - Old info: " + oldCandidate);
        candidateRepository.save(oldCandidate);

        // update candidate
        updateCandidate = candidateMapper.dtoToEntity(request);
        updateCandidate.setCandidateId(candidateId);
        updateCandidate.setCvUrl(oldCandidate.getCvUrl());
        updateCandidate.setAccount(oldCandidate.getAccount());

        if (request.address() == null) {
            updateCandidate.setAddress(oldCandidate.getAddress());
        } else {
            addressService.updateAddress(oldCandidate.getAddress().getAddressId(), request.address());
        }

        System.out.println("Candidate - New info: " + updateCandidate);
        candidateRepository.save(updateCandidate);

        String accessToken = jwtService.generateToken(updateAccount);
        String refreshToken = jwtService.generateRefreshToken(updateAccount);

        accountService.revokeAllAccountTokens(accountId);

        Token tokenAccess = Token
                .builder()
                .account(updateAccount)
                .token(accessToken)
                .tokenType(TokenType.ACCESS)
                .expired(false)
                .revoked(false).build();

        Token tokenRefresh = Token
                .builder()
                .account(updateAccount)
                .token(refreshToken)
                .tokenType(TokenType.REFRESH)
                .expired(false)
                .revoked(false).build();

        tokenService.saveToken(tokenAccess);
        tokenService.saveToken(tokenRefresh);

        return AuthenticationResponseModel
                .builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken).build();
    }

    public void uploadCandidateProfileImageNoToken(Principal connectedCandidate, MultipartFile file) {
        Candidate candidate = getCurrentCandidate(connectedCandidate);
        uploadProfileImage(candidate, file);
    }

    @Transactional
    public void uploadProfileImage(Candidate candidate, MultipartFile file) {
        String fileDir = "img/candidate_profile/candidate_" + candidate.getCandidateId() + "/";

        String imgUrl = fileService.uploadImage(file, fileDir, root);

        System.out.println("Candidate before upload image: " + candidate);

        Candidate oldCandidate = new Candidate(candidate, true);
        candidateRepository.save(oldCandidate);

        // update database with imgUrl
        candidate.setImgUrl(imgUrl);
        System.out.println("Candidate after upload image: " + candidate);

        // save candidate
        candidateRepository.save(candidate);
    }

    @Transactional
    public String uploadCandidateProfileImage(Principal connectedCandidate, MultipartFile file) {
        Candidate candidate = getCurrentCandidate(connectedCandidate);
        return prefix + s3Service.uploadFile("profile-images/%s/".formatted(candidate.getCandidateId()), file);
    }

    public String uploadCandidateCV(Principal connectedCandidate, MultipartFile file) {
        Candidate candidate = getCurrentCandidate(connectedCandidate);
        String cvUrl = prefix + s3Service.uploadFile("cv/%s/".formatted(candidate.getCandidateId()), file);
        return cvUrl;
    }

    public byte[] getCandidateCV(Integer id) {
        Candidate candidate = findCandidateById(id);
        byte[] cv = s3Service.downloadFile("cv/10/1700818252791-2.png");
        return cv;
    }

    public byte[] getCandidateProfileImage(Integer id) {
        Candidate candidate = findCandidateById(id);

        if (StringUtils.isBlank(candidate.getImgUrl())) {
            throw new ResourceNotFoundException("Candidate with id [%s] profile image not found".formatted(id));
        }

        byte[] profileImage = s3Service.downloadFile(candidate.getImgUrl());
        return profileImage;
    }

    public Candidate getCurrentCandidate(Principal connectedCandidate) {
        return findCandidateByEmail(accountService.getCurrentAccount(connectedCandidate).getEmail());
    }

    public CandidateResponseModel getCurrentCandidateDisplay(Principal connectedCandidate) {
        return candidateMapper.entityToDto(getCurrentCandidate(connectedCandidate));
    }

    public void deleteCandidate(Integer id) {
        Candidate candidate = findCandidateById(id);
        candidate.setDeleteFlag(true);

        Account account = candidate.getAccount();
        account.setDeleteFlag(true);

        accountRepository.save(account);
        candidateRepository.save(candidate);

        accountService.revokeAllAccountTokens(account.getId());
    }

    public void changePassword(ChangePasswordDto request,
                               Principal connectedCandidate) {
        Candidate candidate = getCurrentCandidate(connectedCandidate);
        Account account = candidate.getAccount();
        int accountId = candidate.getAccount().getId();

//         check if the current password is correct
        if (!passwordEncoder.matches(request.currentPassword(), account.getPassword())) {
            throw new InputException("Wrong password");
        }
        // check if the two new passwords are the same
        if (!request.newPassword().equals(request.confirmationPassword())) {
            throw new InputException("Password are not the same");
        }

        Account oldAccount = new Account(account, true);
        accountRepository.save(oldAccount);
        System.out.println("Account - before change password: " + oldAccount);

        Account newAccount = Account.builder()
                .id(accountId)
                .email(oldAccount.getEmail())
                .password(passwordEncoder.encode(request.newPassword()))
                .enabled(true)
                .role(oldAccount.getRole())
                .deleteFlag(false)
                .oldId(null)
                .build();
        accountRepository.save(newAccount);
        System.out.println("Account - after change password: " + newAccount);

        System.out.println("Candidate - before change password: " + candidate);
        // kiem tra xem co can thiet hay khong
        candidate.setLastModified(LocalDateTime.now());
        candidate.setLastModifiedBy(accountId);
        candidateRepository.save(candidate);
        System.out.println("Candidate - before change password: " + candidate);

        accountService.revokeAllAccountTokens(accountId);
    }

    public Set<CandidateEducationDto> getCandidateEducation(Integer id) {
        return candidateEducationService.findByCandidate(id);
    }

    public void addCandidateEducation(CandidateEducationDto candidateEducationDto, Principal connectedCandidate) {
        Candidate candidate = getCurrentCandidate(connectedCandidate);
        System.out.println("Candidate before add education: " + candidate);
        System.out.println("List candidate education before add: " + candidate.getCandidateEducations());
        Set<CandidateEducation> list = candidateEducationService.addCandidateEducation(candidate.getCandidateEducations(), candidateEducationDto);
        System.out.println("List candidate education after add: " + list);
        candidate.setCandidateEducations(list);
        candidateRepository.save(candidate);
    }

    public void updateCandidateEducation(Integer id, CandidateEducationDto candidateEducationDto, Principal connectedCandidate) {
        Candidate candidate = getCurrentCandidate(connectedCandidate);
        Set<CandidateEducation> list = candidateEducationService.updateCandidateEducation(id, candidateEducationDto, candidate.getCandidateEducations());
    }

    public void deleteCandidateEducation(Integer id, Principal connectedCandidate) {
        Candidate candidate = getCurrentCandidate(connectedCandidate);
        Set<CandidateEducation> list = candidateEducationService.deleteCandidateEducation(id, candidate.getCandidateEducations());
    }
}
