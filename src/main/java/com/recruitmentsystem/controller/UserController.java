package com.recruitmentsystem.controller;

import com.recruitmentsystem.common.exception.ResourceAlreadyExistsException;
import com.recruitmentsystem.common.exception.ResourceNotFoundException;
import com.recruitmentsystem.model.user.UserDisplayModel;
import com.recruitmentsystem.model.user.UserRequestModel;
import com.recruitmentsystem.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<UserDisplayModel>> getAllUsers() {
        List<UserDisplayModel> users = userService.findAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/getAccountInfo")
    public ResponseEntity<UserDisplayModel> getUserProfile(@RequestParam("token") String token) {
        UserDisplayModel userDisplayModel = userService
                .getUserDisplayByToken(token);
        return ResponseEntity.ok(userDisplayModel);
    }

    @GetMapping("/find/{id}")
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

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity addUser(@RequestBody UserRequestModel request) {
        try {
            userService.addUser(request);
            return ResponseEntity.ok().build();
        } catch (ResourceAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /*
        @RequestBody nói với Spring Boot rằng hãy chuyển Json trong request body
        thành đối tượng UserRequestModel
    */
    @PostMapping("/update/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity updateUser(@PathVariable("id") Integer id,
                                     @RequestBody UserRequestModel request) {
        try {
            userService.updateUser(id, request);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

//    @PostMapping("/upload")
//    public ResponseEntity uploadImage(@RequestParam("image") MultipartFile multipartFile) {
//        try {
//            System.out.println(multipartFile);
//            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
//            System.out.println(fileName);
//            String uploadDir = "user-photos";
//            FileService.saveFile(uploadDir, fileName, multipartFile);
//        } catch (ResourceNotFoundException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
//        } catch (IOException e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//        }
//        return ResponseEntity.ok().build();
//    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity deleteUser(@PathVariable("id") Integer id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

//    @DeleteMapping("/delete")
//    public void deleteUser(@RequestBody Integer[] ids) {
//        Stream
//                .of(ids)
//                .forEach(
//                        id -> userService.deleteUser(id)
//                );
//    }
}
