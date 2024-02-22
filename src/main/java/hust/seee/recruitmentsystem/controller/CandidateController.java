package hust.seee.recruitmentsystem.controller;

import hust.seee.recruitmentsystem.dto.CandidateDTO;
import hust.seee.recruitmentsystem.dto.EducationDTO;
import hust.seee.recruitmentsystem.dto.HistoryDTO;
import hust.seee.recruitmentsystem.response.AuthResponse;
import hust.seee.recruitmentsystem.response.BaseResponse;
import hust.seee.recruitmentsystem.service.CandidateService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CandidateController {
    private final CandidateService candidateService;

    @GetMapping("/admin/manage/candidates/all")
    public List<CandidateDTO> getAllCandidates() {
        return candidateService.findAllCandidates();
    }

    @PostMapping("/admin/manage/candidates/get-all")
    public ResponseEntity<BaseResponse> getAll(@RequestBody CandidateDTO dto) {
        BaseResponse baseResponse = new BaseResponse();
        Page<CandidateDTO> page = candidateService.findAll(dto);
        baseResponse.setAdditionalProperty("data", page.getContent());
        baseResponse.setAdditionalProperty("total", page.getTotalElements());

        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    @GetMapping("/admin/manage/candidates/find/{id}")
    public CandidateDTO getCandidateById(@PathVariable("id") Integer id) {
        return candidateService.findCandidateDTOById(id);
    }

    @GetMapping("/admin/manage/candidates/find")
    public List<CandidateDTO> getCandidateByName(@RequestParam("name") String key) {
        return candidateService.findAllCandidateByKey(key);
    }

    @PostMapping("/admin/manage/candidates/add")
    @ResponseStatus(HttpStatus.CREATED)
    public CandidateDTO addCandidate(@RequestBody CandidateDTO candidateRequest) {
        return candidateService.addCandidate(candidateRequest);
    }

    @DeleteMapping("/admin/manage/candidates/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCandidate(@PathVariable("id") Integer id) {
        candidateService.deleteCandidate(id);
    }

//    @DeleteMapping("/admin/manage/candidates/delete")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public void deleteCandidate(@RequestBody Integer[] ids) {
//        for (Integer id : ids) {
//            candidateService.deleteCandidate(id);
//        }
//    }

    @GetMapping("/candidate/get-info-no-token")
    public CandidateDTO getCandidateProfile(Principal principal) {
        return candidateService.getCurrentCandidateDisplay(principal);
    }

    @PostMapping("/candidate/get-education")
    public Set<EducationDTO> getEducation(@RequestBody CandidateDTO dto) {
        return candidateService.getEducation(dto.getId());
    }

    @PostMapping("/candidate/save-education")
    public void saveEducation(@RequestBody EducationDTO dto, Principal principal) {
        candidateService.saveEducation(dto, principal);
    }

    @PostMapping("/candidate/delete-education")
    public void deleteEducation(@RequestBody EducationDTO dto) {
        candidateService.deleteEducation(dto.getId());
    }

    @PostMapping("/candidate/get-history")
    public Set<HistoryDTO> getHistory(@RequestBody CandidateDTO dto) {
        return candidateService.getHistory(dto.getId());
    }

    @PostMapping("/candidate/save-history")
    public void saveHistory(@RequestBody HistoryDTO dto, Principal principal) {
        candidateService.saveHistory(dto, principal);
    }

    @PostMapping("/candidate/delete-history")
    public void deleteEducation(@RequestBody HistoryDTO dto) {
        candidateService.deleteHistory(dto.getId());
    }

    @PutMapping("/candidate/update-no-token")
    public ResponseEntity<?> updateCandidate(@RequestBody CandidateDTO candidateRequestModel,
                                             Principal principal) {
        AuthResponse response = candidateService.updateCandidateByCandidate(candidateRequestModel, principal);
        return ResponseEntity.ok(response);
    }

    @PostMapping(
            value = "/candidate/image/upload-no-token",
            consumes = {"multipart/form-data"}
    )
    @ResponseStatus(HttpStatus.CREATED)
    public void uploadImageUrl(@RequestParam("image") MultipartFile multipartFile, Principal principal) {
        candidateService.uploadCandidateProfileImageNoToken(principal, multipartFile);
    }

    /**
     * @param file
     * @param principal upload image to AWS S3 Bucket
     */
    @PostMapping(
            value = "/candidate/profile-image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public String uploadCandidateProfileImage(@RequestParam("file") MultipartFile file,
                                              Principal principal) {
        return candidateService.uploadCandidateProfileImage(principal, file);
    }

    @PostMapping(
            value = "/candidate/cv",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public void uploadCandidateCV(@RequestParam("file") MultipartFile file,
                                  Principal principal) {
        candidateService.uploadCandidateCV(principal, file);
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
    public void changePassword(@RequestBody CandidateDTO dto, Principal principal) {
        candidateService.changePassword(dto, principal);
    }
}
