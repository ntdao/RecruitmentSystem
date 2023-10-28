package com.recruitmentsystem.controller;

import com.recruitmentsystem.common.exception.ResourceNotFoundException;
import com.recruitmentsystem.model.auth.AuthenticationResponse;
import com.recruitmentsystem.model.pagination.MyPagination;
import com.recruitmentsystem.model.user.ChangePasswordRequest;
import com.recruitmentsystem.model.user.UserDisplayModel;
import com.recruitmentsystem.model.user.UserRequestModel;
import com.recruitmentsystem.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/manage/users/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getAllUsers() {
        List<UserDisplayModel> users = userService.findAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/manage/users/all-paging")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getAllUsers(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy) {
        MyPagination<UserDisplayModel> list = userService.getAllUsers(pageNo, pageSize, sortBy);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/manage/users/find/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getUserById(@PathVariable("id") Integer id) {
        UserDisplayModel user;
        try {
            user = userService.findById(id);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        return ResponseEntity.ok(user);
    }

    @GetMapping("/manage/users/find")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getUserByName(@RequestParam("name") String name) {
        List<UserDisplayModel> user = userService.findAllUserByName(name);
        return ResponseEntity.ok(user);
    }

    /*
       @RequestBody nói với Spring Boot rằng hãy chuyển Json trong request body
       thành đối tượng UserRequestModel
   */
    @PostMapping("/manage/users/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> addUser(@RequestBody UserRequestModel userRequest,
                                     HttpServletRequest request) {
        try {
            userService.addUser(userRequest, request.getUserPrincipal());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
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

//    @PutMapping("/manage/users/update-role/{user-id}")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    public ResponseEntity<?> updateUser(@PathVariable("user-id") Integer id,
//                                        @RequestBody Role role,
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

    @DeleteMapping("/manage/users/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Integer id,
                                        HttpServletRequest request) {
        try {
            userService.deleteUser(id, request.getUserPrincipal());
            return ResponseEntity.ok().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/manage/users/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteUser(@RequestBody Integer[] ids,
                                        HttpServletRequest request) {
        for (Integer id : ids) {
            try {
                userService.deleteUser(id, request.getUserPrincipal());
            } catch (ResourceNotFoundException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/get-info")
    public ResponseEntity<?> getUserProfile(@RequestParam("token") String token) {
        try {
            UserDisplayModel userDisplayModel = userService
                    .findUserDisplayByToken(token);
            return ResponseEntity.ok(userDisplayModel);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/user/update")
    public ResponseEntity<?> updateUser(@RequestParam("token") String token,
                                        @RequestBody UserRequestModel request) {
        AuthenticationResponse response;
        try {
            response = userService.updateUser(token, request);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token is invalid");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @PutMapping("/user/change-password")
    public ResponseEntity<?> changePassword(@RequestParam("token") String token,
                                            @RequestBody ChangePasswordRequest request) {
        try {
            userService.changePassword(token, request);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/user/image/upload", consumes = {"multipart/form-data"})
    public ResponseEntity<?> uploadImage(@RequestParam("token") String token,
                                         @RequestParam("image") MultipartFile multipartFile) {
        try {
            userService.uploadUserProfileImage(token, multipartFile);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    // khong su dung token
    @GetMapping("/user/get-info-no-token")
    public ResponseEntity<?> getUserProfile(HttpServletRequest request) {
        UserDisplayModel userDisplayModel;
        try {
            userDisplayModel = userService.getCurrentUserDisplay(request.getUserPrincipal());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        return ResponseEntity.ok(userDisplayModel);
    }

    @PutMapping("/user/update-no-token")
    public ResponseEntity<?> updateUser(@RequestBody UserRequestModel userRequestModel,
                                        HttpServletRequest request) {
        AuthenticationResponse response;
        try {
            response = userService.updateUserByAuth(userRequestModel, request.getUserPrincipal());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token is invalid");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

//    @PostMapping("/user/image/upload-no-token")
//    public ResponseEntity uploadImage(@RequestParam("image") MultipartFile multipartFile,
//                                      HttpServletRequest request) {
//        try {
//            userService.uploadUserProfileImageNoToken(request.getUserPrincipal(), multipartFile);
//        } catch (ResourceNotFoundException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
//        } catch (IOException e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//        }
//        return ResponseEntity.ok().build();
//    }

    @PostMapping(value = "/user/image/upload-no-token", consumes = {"multipart/form-data"})
    public ResponseEntity<?> uploadImageUrl(@RequestParam("image") MultipartFile multipartFile,
                                            HttpServletRequest request) {
        try {
            System.out.println("File size: " + multipartFile.getSize());
            userService.uploadUserProfileImageNoToken(request.getUserPrincipal(), multipartFile);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/user/change-password-no-token")
    public ResponseEntity<?> changePassword(
            @RequestBody ChangePasswordRequest passwordRequest,
            HttpServletRequest request) {
        try {
            userService.changePassword(passwordRequest, request.getUserPrincipal());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        return ResponseEntity.ok().build();
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
