package com.recruitmentsystem.controller;

import com.recruitmentsystem.common.exception.ResourceAlreadyExistsException;
import com.recruitmentsystem.common.exception.ResourceNotFoundException;
import com.recruitmentsystem.model.role.RoleDisplayModel;
import com.recruitmentsystem.model.role.RoleRequestModel;
import com.recruitmentsystem.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class RoleController {
    private final RoleService roleService;

    @GetMapping("/all")
    public ResponseEntity<List<RoleDisplayModel>> getAllRoles() {
        List<RoleDisplayModel> roles = roleService.findAllRoles();
        return ResponseEntity.ok(roles);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<?> getRoleById(@PathVariable("id") Integer id) {
        RoleDisplayModel role;
        try {
            role = roleService.findById(id);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        return ResponseEntity.ok(role);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addRole(@RequestBody RoleRequestModel request) {
        try {
            roleService.addRole(request);
        } catch (ResourceAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<?> updateRole(@PathVariable("id") Integer id,
                                        @RequestBody RoleRequestModel request) {
        try {
            roleService.updateRole(id, request);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteRole(@PathVariable("id") Integer id) {
        try {
            roleService.deleteRole(id);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

//    @DeleteMapping("/delete")
//    public void deleteRole(@RequestBody Integer[] ids) {
//        Stream.of(ids).forEach(id -> roleService.deleteRole(id));
//    }
}
