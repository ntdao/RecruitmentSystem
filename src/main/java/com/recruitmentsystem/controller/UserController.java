package com.recruitmentsystem.controller;

import com.recruitmentsystem.model.user.UserRequestModel;
import com.recruitmentsystem.model.user.UserDisplayModel;
import com.recruitmentsystem.service.IUserService;
import jakarta.annotation.security.RolesAllowed;
import lombok.AllArgsConstructor;
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
public class UserController {
    private final IUserService userService;

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDisplayModel>> getAllUsers() {
        List<UserDisplayModel> users = userService.findAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/find/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDisplayModel> getUserById(@PathVariable("id") Integer id) {
        UserDisplayModel user = userService.findById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public void registerUser(@RequestBody UserRequestModel request) {
        userService.addUser(request);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void updateUser(@PathVariable("id") Integer id, @RequestBody UserRequestModel request) {
        userService.updateUser(id, request);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(@PathVariable("id") Integer id) {
        userService.deleteUser(id);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(@RequestBody Integer[] ids) {
        Stream.of(ids).forEach(id -> userService.deleteUser(id));
    }
}
