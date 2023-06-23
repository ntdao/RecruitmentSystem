package com.recruitmentsystem.controller;

import com.recruitmentsystem.common.exception.ResourceAlreadyExistsException;
import com.recruitmentsystem.common.exception.ResourceNotFoundException;
import com.recruitmentsystem.model.TestResponse;
import com.recruitmentsystem.model.user.UserRequestModel;
import com.recruitmentsystem.model.user.UserDisplayModel;
import com.recruitmentsystem.service.impl.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class UserController {
    private final UserService userService;

    @GetMapping("/all")
    public TestResponse<List<UserDisplayModel>> getAllUsers() {
        List<UserDisplayModel> users = userService.findAllUsers();
        return new TestResponse(0, "OK", users);
    }

    /*
        phần path URL bạn muốn lấy thông tin sẽ để trong ngoặc kép {}
     */
    @GetMapping("/find/{id}")
    // @PathVariable lấy ra thông tin trong URL
    // dựa vào tên của thuộc tính đã định nghĩa trong ngoặc kép /find/{id}
    public TestResponse<UserDisplayModel> getUserById(@PathVariable("id") Integer id) {
        UserDisplayModel user;
        try {
            user = userService.findById(id);
        }
        catch (ResourceNotFoundException e) {
            return new TestResponse(-1,e.getMessage());
        }
        return new TestResponse(0, "OK", user);
    }

    @PostMapping("/add")
    public TestResponse addUser(@RequestBody UserRequestModel request) {
        try {
            userService.addUser(request);
           return new TestResponse(0, "success");
        }
        catch (ResourceAlreadyExistsException e) {
            return new TestResponse(-1,e.getMessage());
        }
    }

    /*
        @RequestBody nói với Spring Boot rằng hãy chuyển Json trong request body
        thành đối tượng UserRequestModel
    */
    @PutMapping("/update/{id}")
    public TestResponse updateUser(@PathVariable("id") Integer id, @RequestBody UserRequestModel request) {
        try {
            userService.updateUser(id, request);
            return new TestResponse(0, "success");
        }
        catch (ResourceNotFoundException e) {
            return new TestResponse(-1,e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public TestResponse deleteUser(@PathVariable("id") Integer id) {
        try {
            userService.deleteUser(id);
            return new TestResponse(0, "success");
        }
        catch (ResourceNotFoundException e) {
            return new TestResponse(-1,e.getMessage());
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
