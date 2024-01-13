package com.recruitmentsystem.controller;

import com.recruitmentsystem.dto.*;
import com.recruitmentsystem.pagination.PageDto;
import com.recruitmentsystem.service.CandidateService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CandidateController {
    private final CandidateService candidateService;

    @GetMapping("/admin/manage/candidates/all")
    public List<CandidateResponseModel> getAllCandidates() {
        return candidateService.findAllCandidates();
    }

    @PostMapping("/admin/manage/candidates/search")
    public Page<CandidateResponseModel> searchCandidate(@RequestBody PageDto pageDto) {
        return candidateService.searchCandidate(pageDto);
    }

    @GetMapping("/admin/manage/candidates/find/{id}")
    public CandidateResponseModel getCandidateById(@PathVariable("id") Integer id) {
        return candidateService.findCandidateResponseModelById(id);
    }

    @GetMapping("/admin/manage/candidates/find")
    public List<CandidateResponseModel> getCandidateByName(@RequestParam("name") String name) {
        return candidateService.findAllCandidateByName(name);
    }

    @PostMapping("/admin/manage/candidates/add")
    @ResponseStatus(HttpStatus.CREATED)
    public CandidateResponseModel addCandidate(@RequestBody CandidateRequestModel candidateRequest) {
        return candidateService.addCandidate(candidateRequest);
    }

    @DeleteMapping("/admin/manage/candidates/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCandidate(@PathVariable("id") Integer id) {
        candidateService.deleteCandidate(id);
    }

    @DeleteMapping("/admin/manage/candidates/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCandidate(@RequestBody Integer[] ids) {
        for (Integer id : ids) {
            candidateService.deleteCandidate(id);
        }
    }

    @GetMapping("/candidate/get-info-no-token")
    public CandidateResponseModel getCandidateProfile(Principal connectedCandidate) {
        return candidateService.getCurrentCandidateDisplay(connectedCandidate);
    }

    @PostMapping("/candidate/add-candidate-education")
    public void addCandidateEducation(@RequestBody CandidateEducationDto candidateEducationDto,
                                 Principal connectedCandidate) {
        candidateService.addCandidateEducation(candidateEducationDto, connectedCandidate);
    }

    @PutMapping("/candidate/update-no-token")
    public ResponseEntity<?> updateCandidate(@RequestBody CandidateRequestModel candidateRequestModel,
                                        Principal connectedCandidate) {
        AuthenticationResponseModel response = candidateService.updateCandidateByCandidate(candidateRequestModel, connectedCandidate);
        return ResponseEntity.ok(response);
    }

    @PostMapping(
            value = "/candidate/image/upload-no-token",
            consumes = {"multipart/form-data"}
    )
    @ResponseStatus(HttpStatus.CREATED)
    public void uploadImageUrl(@RequestParam("image") MultipartFile multipartFile, Principal connectedCandidate) {
        candidateService.uploadCandidateProfileImageNoToken(connectedCandidate, multipartFile);
    }

    /**
     * @param file
     * @param connectedCandidate upload image to AWS S3 Bucket
     */
    @PostMapping(
            value = "/candidate/profile-image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public String uploadCandidateProfileImage(@RequestParam("file") MultipartFile file,
                                         Principal connectedCandidate) {
        return candidateService.uploadCandidateProfileImage(connectedCandidate, file);
    }

    @PostMapping(
            value = "/candidate/cv",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public void uploadCandidateCV(@RequestParam("file") MultipartFile file,
                             Principal connectedCandidate) {
        candidateService.uploadCandidateCV(connectedCandidate, file);
    }

    @GetMapping(
            value = "/candidate/{candidateId}/cv",
            produces = MediaType.APPLICATION_PDF_VALUE
    )
    public byte[] getCandidateCV(
            @PathVariable("candidateId") Integer candidateId) {
        return candidateService.getCandidateCV(candidateId);
    }

    @GetMapping(
            value = "/candidate/{candidateId}/profile-image",
            produces = MediaType.IMAGE_JPEG_VALUE
    )
    public byte[] getCandidateProfileImage(@PathVariable("candidateId") Integer candidateId) {
        return candidateService.getCandidateProfileImage(candidateId);
    }

    @PatchMapping("/candidate/change-password-no-token")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(@RequestBody ChangePasswordDto passwordRequest, Principal connectedCandidate) {
        candidateService.changePassword(passwordRequest, connectedCandidate);
    }
}
