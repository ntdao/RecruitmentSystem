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
    private final CandidateService userService;

    @GetMapping("/admin/manage/users/all")
    public List<CandidateResponseModel> getAllCandidates() {
        return userService.findAllCandidates();
    }

    @PostMapping("/admin/manage/users/search")
    public Page<CandidateResponseModel> searchCandidate(@RequestBody PageDto pageDto) {
        return userService.searchCandidate(pageDto);
    }

    @GetMapping("/admin/manage/users/find/{id}")
    public CandidateResponseModel getCandidateById(@PathVariable("id") Integer id) {
        return userService.findCandidateResponseModelById(id);
    }

    @GetMapping("/admin/manage/users/find")
    public List<CandidateResponseModel> getCandidateByName(@RequestParam("name") String name) {
        return userService.findAllCandidateByName(name);
    }

    @PostMapping("/admin/manage/users/add")
    @ResponseStatus(HttpStatus.CREATED)
    public CandidateResponseModel addCandidate(@RequestBody CandidateRequestModel userRequest) {
        return userService.addCandidate(userRequest);
    }

    @DeleteMapping("/admin/manage/users/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCandidate(@PathVariable("id") Integer id) {
        userService.deleteCandidate(id);
    }

    @DeleteMapping("/admin/manage/users/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCandidate(@RequestBody Integer[] ids) {
        for (Integer id : ids) {
            userService.deleteCandidate(id);
        }
    }

    @GetMapping("/user/get-info-no-token")
    public CandidateResponseModel getCandidateProfile(Principal connectedCandidate) {
        return userService.getCurrentCandidateDisplay(connectedCandidate);
    }

    @PostMapping("/user/add-user-education")
    public void addCandidateEducation(@RequestBody CandidateEducationDto candidateEducationDto,
                                 Principal connectedCandidate) {
        userService.addCandidateEducation(candidateEducationDto, connectedCandidate);
    }

    @PutMapping("/user/update-no-token")
    public ResponseEntity<?> updateCandidate(@RequestBody CandidateRequestModel candidateRequestModel,
                                        Principal connectedCandidate) {
        AuthenticationResponseModel response = userService.updateCandidateByCandidate(candidateRequestModel, connectedCandidate);
        return ResponseEntity.ok(response);
    }

    @PostMapping(
            value = "/user/image/upload-no-token",
            consumes = {"multipart/form-data"}
    )
    @ResponseStatus(HttpStatus.CREATED)
    public void uploadImageUrl(@RequestParam("image") MultipartFile multipartFile, Principal connectedCandidate) {
        userService.uploadCandidateProfileImageNoToken(connectedCandidate, multipartFile);
    }

    /**
     * @param file
     * @param connectedCandidate upload image to AWS S3 Bucket
     */
    @PostMapping(
            value = "/user/profile-image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public String uploadCandidateProfileImage(@RequestParam("file") MultipartFile file,
                                         Principal connectedCandidate) {
        return userService.uploadCandidateProfileImage(connectedCandidate, file);
    }

    @PostMapping(
            value = "/user/cv",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public void uploadCandidateCV(@RequestParam("file") MultipartFile file,
                             Principal connectedCandidate) {
        userService.uploadCandidateCV(connectedCandidate, file);
    }

    @GetMapping(
            value = "/user/{userId}/cv",
            produces = MediaType.APPLICATION_PDF_VALUE
    )
    public byte[] getCandidateCV(
            @PathVariable("userId") Integer userId) {
        return userService.getCandidateCV(userId);
    }

    @GetMapping(
            value = "/user/{userId}/profile-image",
            produces = MediaType.IMAGE_JPEG_VALUE
    )
    public byte[] getCandidateProfileImage(@PathVariable("userId") Integer userId) {
        return userService.getCandidateProfileImage(userId);
    }

    @PatchMapping("/user/change-password-no-token")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(@RequestBody ChangePasswordDto passwordRequest, Principal connectedCandidate) {
        userService.changePassword(passwordRequest, connectedCandidate);
    }
}
