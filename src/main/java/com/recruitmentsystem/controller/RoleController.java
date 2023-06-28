package com.recruitmentsystem.controller;

import com.recruitmentsystem.model.role.RoleDisplayModel;
import com.recruitmentsystem.model.role.RoleRequestModel;
import com.recruitmentsystem.service.IRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class RoleController {
    private final IRoleService roleService;

    @GetMapping("/all")
    public ResponseEntity<List<RoleDisplayModel>> getAllRoles() {
        List<RoleDisplayModel> roles = roleService.findAllRoles();
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<RoleDisplayModel> getRoleById(@PathVariable("id") Integer id) {
        RoleDisplayModel role = roleService.findById(id);
        return new ResponseEntity<>(role, HttpStatus.OK);
    }

    @PostMapping("/add")
    public void addRole(@RequestBody RoleRequestModel request) {
        roleService.addRole(request);
    }

    @PutMapping("/update/{id}")
    public void updateRole(@PathVariable("id") Integer id, @RequestBody RoleRequestModel request) {
        roleService.updateRole(id, request);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteRole(@PathVariable("id") Integer id) {
        roleService.deleteRole(id);
    }

    @DeleteMapping("/delete")
    public void deleteRole(@RequestBody Integer[] ids) {
        Stream.of(ids).forEach(id -> roleService.deleteRole(id));
    }
}
