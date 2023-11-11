package com.recruitmentsystem.user;

import com.recruitmentsystem.auth.AuthenticationResponseModel;
import com.recruitmentsystem.pagination.MyPagination;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<UserResponseModel> getAllUsers() {
        return userService.findAllUsers();
    }

    @GetMapping("/admin/manage/users/all-paging")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PreAuthorize("hasAuthority('ADMIN')")
    public MyPagination<UserResponseModel> getAllUsers(@RequestParam(defaultValue = "0") Integer pageNo,
                                                       @RequestParam(defaultValue = "10") Integer pageSize,
                                                       @RequestParam(defaultValue = "id") String sortBy) {
        return userService.getAllUsers(pageNo, pageSize, sortBy);
    }

    @GetMapping("/admin/manage/users/find/{id}")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PreAuthorize("hasAuthority('ADMIN')")
    public UserResponseModel getUserById(@PathVariable("id") Integer id) {
        return userService.findById(id);
    }

    @GetMapping("/admin/manage/users/find")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<UserResponseModel> getUserByName(@RequestParam("name") String name) {
        return userService.findAllUserByName(name);
    }

    /*
       @RequestBody nói với Spring Boot rằng hãy chuyển Json trong request body
       thành đối tượng UserRequestModel
   */
    @PostMapping("/admin/manage/users/add")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseModel addUser(@RequestBody UserRequestModel userRequest) {
        return userService.addUser(userRequest);
    }

//    @PutMapping("/manage/users/update/{id}")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    public ResponseEntity<?> updateUser(@PathVariable("id") Integer id,
//                                        @RequestBody UserRequestModel request,
//                                        Principal connectedUser) {
//        try {
//            userService.updateUser(id, request, connectedUser);
////        } catch (ResourceNotModifiedException e) {
////            return ResponseEntity.ok(e.getMessage());
//        } catch (ResourceNotFoundException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//        }
//        return ResponseEntity.ok().build();
//    }

    @DeleteMapping("/admin/manage/users/delete/{id}")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable("id") Integer id) {
        userService.deleteUser(id);
    }

    @DeleteMapping("/admin/manage/users/delete")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@RequestBody Integer[] ids) {
        for (Integer id : ids) {
            userService.deleteUser(id);
        }
    }

//    @GetMapping("/user/get-info")
//    public UserResponseModel getUserProfile(@RequestParam("token") String token) {
//        return userService.findUserDisplayByToken(token);
//    }

//    @PutMapping("/user/update")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public AuthenticationResponseModel updateUser(@RequestParam("token") String token,
//                                                  @RequestBody UserRequestModel request) {
//        return userService.updateUserByToken(token, request);
//    }

//    @PutMapping("/user/change-password")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public void changePassword(@RequestParam("token") String token,
//                               @RequestBody ChangePasswordRequestModel request) {
//        userService.changePassword(token, request);
//    }

//    @PostMapping(value = "/user/image/upload", consumes = {"multipart/form-data"})
//    @ResponseStatus(HttpStatus.CREATED)
//    public void uploadImage(@RequestParam("token") String token,
//                            @RequestParam("image") MultipartFile multipartFile) {
//        userService.uploadUserProfileImage(token, multipartFile);
//    }

    /**
        Không sử dụng token
        Sử dụng Principal
     */
    @GetMapping("/user/get-info-no-token")
    public UserResponseModel getUserProfile(Principal connectedUser) {
        return userService.getCurrentUserDisplay(connectedUser);
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

    @PostMapping(value = "/user/image/upload-no-token", consumes = {"multipart/form-data"})
    @ResponseStatus(HttpStatus.CREATED)
    public void uploadImageUrl(@RequestParam("image") MultipartFile multipartFile, Principal connectedUser) {
        System.out.println("File size: " + multipartFile.getSize());
        userService.uploadUserProfileImageNoToken(connectedUser, multipartFile);
    }

    @PatchMapping("/user/change-password-no-token")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(@RequestBody ChangePasswordRequestModel passwordRequest, Principal connectedUser) {
        userService.changePassword(passwordRequest, connectedUser);
    }

//    @GetMapping("/user/image/download")
//    public ResponseEntity downloadFile1(@RequestParam String fileName) throws IOException {
//        File file = new File(fileName);
//        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName())
//                .contentType(MediaType.APPLICATION_OCTET_STREAM)
//                .contentLength(file.length())
//                .body(resource);
//    }
//
//    @GetMapping("/user/image/{fileName}")
//    public ResponseEntity<?> downloadImage(@PathVariable String fileName){
//        byte[] imageData = userService.downloadImage(fileName);
//        return ResponseEntity.status(HttpStatus.OK)
//                .contentType(MediaType.valueOf("image/png"))
//                .body(imageData);
//
//    }
}
