package com.recruitmentsystem.controller;

import com.recruitmentsystem.common.exception.ResourceNotFoundException;
import com.recruitmentsystem.common.exception.ResourceNotModifiedException;
import com.recruitmentsystem.model.pagination.MyPagination;
import com.recruitmentsystem.model.user.UserChangePassword;
import com.recruitmentsystem.model.user.UserDisplayModel;
import com.recruitmentsystem.model.user.UserRequestModel;
import com.recruitmentsystem.security.auth.AuthenticationResponse;
import com.recruitmentsystem.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

    @GetMapping("/manage_users/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getAllUsers() {
        List<UserDisplayModel> users = userService.findAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/manage_users")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getAllUsers(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy) {
        MyPagination<UserDisplayModel> list = userService.getAllUsers(pageNo, pageSize, sortBy);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/user/getAccountInfo")
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

//    @GetMapping("/user/getInfo")
//    public ResponseEntity<?> getUserProfile() {
//        try {
//            UserDisplayModel userDisplayModel = userService.getCurrentUserDisplay();
//            return ResponseEntity.ok(userDisplayModel);
//        } catch (ResourceNotFoundException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//        }
//    }

    @GetMapping("/manage_users/find/{id}")
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

    @GetMapping("/manage_users/find")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getUserByName(@RequestParam("name") String name) {
        List<UserDisplayModel> user = userService.findAllUserByName(name);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/manage_users/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> addUser(@RequestBody UserRequestModel request) {
        try {
            userService.addUser(request);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /*
        @RequestBody nói với Spring Boot rằng hãy chuyển Json trong request body
        thành đối tượng UserRequestModel
    */
    @PutMapping("/manage_users/update/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateUser(@PathVariable("id") Integer id,
                                        @RequestBody UserRequestModel request) {
        try {
            userService.updateUser(id, request);
        } catch (ResourceNotModifiedException e) {
            return ResponseEntity.ok(e.getMessage());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    @PutMapping("/user/update")
    public ResponseEntity<?> updateUser(@RequestParam("token") String token,
                                        @RequestBody UserRequestModel request) {
        AuthenticationResponse response;
        try {
            response = userService.updateUser(token, request);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

//    @PutMapping("/user/updateUser")
//    public ResponseEntity<?> updateUser(@RequestBody UserRequestModel request) {
//        AuthenticationResponse response;
//        try {
//            response = userService.updateUserByAuth(request);
//        } catch (ResourceNotFoundException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//        }
//        return ResponseEntity.ok(response);
//    }

    @PostMapping("/user/image/upload")
    public ResponseEntity uploadImage(@RequestParam("token") String token,
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

    @GetMapping("/user/image/{fileName}")
    public ResponseEntity<?> downloadImage(@PathVariable String fileName){
        byte[] imageData = userService.downloadImage(fileName);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(imageData);

    }

    @DeleteMapping("/manage_users/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Integer id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/manage_users/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteUser(@RequestBody Integer[] ids) {
        for (Integer id : ids) {
            try {
                userService.deleteUser(id);
            } catch (ResourceNotFoundException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
        }
        return ResponseEntity.ok().build();
    }

    @PutMapping("/user/changePassword")
    public ResponseEntity<?> changePassword(@RequestParam("token") String token,
                                            @RequestBody UserChangePassword request) {
        try {
            userService.changePassword(token, request);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }
}
