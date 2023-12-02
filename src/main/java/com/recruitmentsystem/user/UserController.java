package com.recruitmentsystem.user;

import com.recruitmentsystem.auth.AuthenticationResponseModel;
import com.recruitmentsystem.pagination.MyPagination;
import com.recruitmentsystem.usereducation.UserEducationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/admin/manage/users/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<UserResponseModel> getAllUsers() {
        return userService.findAllUsers();
    }

    @GetMapping("/admin/manage/users/all-paging")
    @PreAuthorize("hasAuthority('ADMIN')")
    public MyPagination<UserResponseModel> getAllUsers(@RequestParam(defaultValue = "0") Integer pageNo,
                                                       @RequestParam(defaultValue = "10") Integer pageSize,
                                                       @RequestParam(defaultValue = "id") String sortBy) {
        return userService.getAllUsers(pageNo, pageSize, sortBy);
    }

    @GetMapping("/admin/manage/users/find/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public UserResponseModel getUserById(@PathVariable("id") Integer id) {
        return userService.findUserResponseModelById(id);
    }

    @GetMapping("/admin/manage/users/find")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<UserResponseModel> getUserByName(@RequestParam("name") String name) {
        return userService.findAllUserByName(name);
    }

    @PostMapping("/admin/manage/users/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseModel addUser(@RequestBody UserRequestModel userRequest) {
        return userService.addUser(userRequest);
    }

    @DeleteMapping("/admin/manage/users/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable("id") Integer id) {
        userService.deleteUser(id);
    }

    @DeleteMapping("/admin/manage/users/delete")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@RequestBody Integer[] ids) {
        for (Integer id : ids) {
            userService.deleteUser(id);
        }
    }

    @GetMapping("/user/get-info-no-token")
    public UserResponseModel getUserProfile(Principal connectedUser) {
        return userService.getCurrentUserDisplay(connectedUser);
    }

    @GetMapping("/user/get-user-education")
    public List<UserEducationDto> getUserEducation(Principal connectedUser) {
        return userService.getUserEducation(connectedUser);
    }

    @PostMapping("/user/add-user-education")
    public void addUserEducation(@RequestBody UserEducationDto userEducationDto,
                                 Principal connectedUser) {
        userService.addUserEducation(userEducationDto, connectedUser);
    }

    @PutMapping("/user/update-no-token")
    public ResponseEntity<?> updateUser(@RequestBody UserRequestModel userRequestModel,
                                     Principal connectedUser) {
        AuthenticationResponseModel response = userService.updateUserByUser(userRequestModel, connectedUser);
        if (response == null) {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping(
            value = "/user/image/upload-no-token",
            consumes = {"multipart/form-data"}
    )
    @ResponseStatus(HttpStatus.CREATED)
    public void uploadImageUrl(@RequestParam("image") MultipartFile multipartFile, Principal connectedUser) {
        userService.uploadUserProfileImageNoToken(connectedUser, multipartFile);
    }

    /**
     *
     * @param file
     * @param connectedUser
     *
     * upload image to AWS S3 Bucket
     */
    @PostMapping(
            value = "/user/profile-image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public void uploadUserProfileImage(@RequestParam("file") MultipartFile file,
                                           Principal connectedUser) {
        userService.uploadUserProfileImage(connectedUser, file);
    }

    @PostMapping(
            value = "/user/cv",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public void uploadUserCV(@RequestParam("file") MultipartFile file,
                                           Principal connectedUser) {
        userService.uploadUserCV(connectedUser, file);
    }

    @GetMapping(
            value = "/user/{userId}/cv",
            produces = MediaType.APPLICATION_PDF_VALUE
    )
    public byte[] getUserCV(
            @PathVariable("userId") Integer userId) {
        return userService.getUserCV(userId);
    }

    @GetMapping(
            value = "/user/{userId}/profile-image",
            produces = MediaType.IMAGE_JPEG_VALUE
    )
    public byte[] getUserProfileImage(@PathVariable("userId") Integer userId) {
        return userService.getUserProfileImage(userId);
    }

    @PatchMapping("/user/change-password-no-token")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(@RequestBody ChangePasswordRequestModel passwordRequest, Principal connectedUser) {
        userService.changePassword(passwordRequest, connectedUser);
    }
}
