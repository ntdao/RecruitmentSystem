package hust.seee.recruitmentsystem.service;

import hust.seee.recruitmentsystem.dto.CandidateDTO;
import hust.seee.recruitmentsystem.dto.EducationDTO;
import hust.seee.recruitmentsystem.dto.HistoryDTO;
import hust.seee.recruitmentsystem.entity.Account;
import hust.seee.recruitmentsystem.entity.Candidate;
import hust.seee.recruitmentsystem.enums.TokenType;
import hust.seee.recruitmentsystem.exception.InputException;
import hust.seee.recruitmentsystem.exception.ResourceNotFoundException;
import hust.seee.recruitmentsystem.mapper.CandidateMapper;
import hust.seee.recruitmentsystem.repository.AccountRepository;
import hust.seee.recruitmentsystem.repository.CandidateRepository;
import hust.seee.recruitmentsystem.response.AuthResponse;
import hust.seee.recruitmentsystem.security.jwt.JwtService;
import hust.seee.recruitmentsystem.security.token.Token;
import hust.seee.recruitmentsystem.security.token.TokenService;
import hust.seee.recruitmentsystem.utils.DataFormat;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;
import java.util.Map;
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
    private final CandidateWorkingHistoryService candidateWorkingHistoryService;
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
    public CandidateDTO addCandidate(CandidateDTO request) {
        accountService.checkDuplicateEmail(request.getEmail());
        Account account = Account.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(roleService.findByName("CANDIDATE"))
                .enabled(true)
                .build();
        accountRepository.save(account);

        addressService.saveAddress(request.getAddress());

        Candidate candidate = candidateMapper.dtoToEntity(request);
        candidate.setAccount(account);
        candidateRepository.save(candidate);

        // bị lỗi trường lastModified và lastModifiedBy
        return candidateMapper.entityToDto(candidate);
    }

    public List<CandidateDTO> findAllCandidates() {
        return candidateRepository.findAllCandidate().stream()
                .map(candidateMapper::entityToDto).collect(Collectors.toList());
    }

//    public Page<CandidateDTO> searchCandidate(CandidateDto dto) {
//        Pageable paging = PageRequest.of(dto.page() - 1, dto.size());
//        Page<Candidate> pagedResult = candidateRepository.findAllCandidate(dto.key(), paging);
//        return pagedResult.map(candidateMapper::entityToDto);
//    }

    public Page<CandidateDTO> findAll(CandidateDTO dto) {
        Pageable pageable = PageRequest.of(dto.getPage() - 1, dto.getSize());
        return candidateRepository.findAll(
                        DataFormat.lower(dto.getFullName()),
                        dto.getCategoryId(),
                        DataFormat.lower(dto.getProvinceCode()),
                        pageable)
                .map(candidateMapper::entityToDto);
    }

    public CandidateDTO findCandidateDTOById(Integer id) {
        return candidateMapper.entityToDto(findCandidateById(id));
    }

    private Candidate findCandidateById(Integer id) {
        return candidateRepository.findCandidateById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate with id " + id + " does not exist"));
    }

    private List<Candidate> findListCandidateByIds(List<Integer> ids) {
        return candidateRepository.findListCandidateByIds(ids);
    }

    public List<CandidateDTO> findCandidatesByIds(List<Integer> ids) {
        return findListCandidateByIds(ids)
                .stream()
                .map(candidateMapper::entityToDto)
                .toList();
    }

    public List<CandidateDTO> findAllCandidateByKey(String key) {
        return candidateRepository.findAllCandidateByKey(DataFormat.lower(key))
                .stream()
                .map(candidateMapper::entityToDto)
                .collect(Collectors.toList());
    }

    public Candidate findCandidateByEmail(String email) {
        return candidateRepository.findCandidateByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate with email " + email + " does not exist"));
    }

    public AuthResponse updateCandidateByCandidate(CandidateDTO request, Principal principal) {
        Candidate updateCandidate = getCurrentCandidate(principal);
        return updateCandidate(updateCandidate, request);
    }

    @Transactional
    public AuthResponse updateCandidate(Candidate updateCandidate, CandidateDTO request) {
        int candidateId = updateCandidate.getCandidateId();
        Account updateAccount = updateCandidate.getAccount();
        int accountId = updateAccount.getId();

        boolean isEmailChange = !updateCandidate.getAccount().getEmail().equals(request.getEmail());
        if (isEmailChange) {
            accountService.checkDuplicateEmail(request.getEmail());
//            Account oldAccount = new Account(updateAccount, true);
//            System.out.println("Account - before update: " + oldAccount);
//            accountRepository.save(oldAccount);
            updateAccount.setEmail(request.getEmail());
//            System.out.println("Account - after update: " + updateAccount);
            accountRepository.save(updateAccount);
        }

//        // tao ban ghi luu thong tin cu cua candidate
//        Candidate oldCandidate = new Candidate(updateCandidate, true);
////        oldCandidate.setAccount(updateAccount);
//        System.out.println("Candidate - Old info: " + oldCandidate);
//        candidateRepository.save(oldCandidate);

        // update candidate
        updateCandidate = candidateMapper.dtoToEntity(request);
        updateCandidate.setCandidateId(candidateId);
//        updateCandidate.setCvUrl(oldCandidate.getCvUrl());
//        updateCandidate.setAccount(oldCandidate.getAccount());

        if (request.getAddress() != null) {
//            updateCandidate.setAddress(oldCandidate.getAddress());
//        } else {
            addressService.updateAddress(updateCandidate.getAddress().getAddressId(), request.getAddress());
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

        return AuthResponse
                .builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken).build();
    }

    public void uploadCandidateProfileImageNoToken(Principal principal, MultipartFile file) {
        Candidate candidate = getCurrentCandidate(principal);
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
    public String uploadCandidateProfileImage(Principal principal, MultipartFile file) {
        Candidate candidate = getCurrentCandidate(principal);
        return prefix + s3Service.uploadFile("profile-images/%s/".formatted(candidate.getCandidateId()), file);
    }

    public String uploadCandidateCV(Principal principal, MultipartFile file) {
        Candidate candidate = getCurrentCandidate(principal);
        return prefix + s3Service.uploadFile("cv/%s/".formatted(candidate.getCandidateId()), file);
    }

    public byte[] getCandidateCV(Integer id) {
        Candidate candidate = findCandidateById(id);
        return s3Service.downloadFile("cv/10/1700818252791-2.png");
    }

    public byte[] getCandidateProfileImage(Integer id) {
        Candidate candidate = findCandidateById(id);

        if (StringUtils.isBlank(candidate.getImgUrl())) {
            throw new ResourceNotFoundException("Candidate with id [%s] profile image not found".formatted(id));
        }

        return s3Service.downloadFile(candidate.getImgUrl());
    }

    public Candidate getCurrentCandidate(Principal principal) {
        return findCandidateByEmail(accountService.getCurrentAccount(principal).getEmail());
    }

    public CandidateDTO getCurrentCandidateDisplay(Principal principal) {
        return candidateMapper.entityToDto(getCurrentCandidate(principal));
    }

    @Transactional
    public void deleteCandidate(Integer id) {
        Candidate candidate = findCandidateById(id);
        candidate.setDeleteFlag(true);

        Account account = candidate.getAccount();
        account.setDeleteFlag(true);

        accountRepository.save(account);
        candidateRepository.save(candidate);

        accountService.revokeAllAccountTokens(account.getId());
    }

    @Transactional
    public void changePassword(CandidateDTO request, Principal principal) {
        Candidate candidate = getCurrentCandidate(principal);
        Account account = candidate.getAccount();
        int accountId = candidate.getAccount().getId();

//         check if the current password is correct
        if (!passwordEncoder.matches(request.getCurrentPassword(), account.getPassword())) {
            throw new InputException("Wrong password");
        }

        account.setPassword(passwordEncoder.encode(request.getNewPassword()));
        accountRepository.save(account);

        accountService.revokeAllAccountTokens(accountId);
    }

    public Set<EducationDTO> getEducation(Integer id) {
        return candidateEducationService.findByCandidate(id);
    }

    public void saveEducation(EducationDTO dto, Principal principal) {
        Candidate candidate = getCurrentCandidate(principal);
        candidateEducationService.save(candidate, dto);
    }

    public void deleteEducation(Integer id) {
        candidateEducationService.delete(id);
    }

    public Set<HistoryDTO> getHistory(Integer id) {
        return candidateWorkingHistoryService.findByCandidate(id);
    }

    public void saveHistory(HistoryDTO dto, Principal principal) {
        Candidate candidate = getCurrentCandidate(principal);
        candidateWorkingHistoryService.save(candidate, dto);
    }

    public void deleteHistory(Integer id) {
        candidateWorkingHistoryService.delete(id);
    }

//    public StatisticDTO getQuantity() {
//        List<Map<String, Object>> map = candidateRepository.getQuantity();
//        return Utils.getStatistic(map);
//    }
}
