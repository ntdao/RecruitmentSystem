package com.recruitmentsystem.controller;

import com.recruitmentsystem.dto.RoleDto;
import com.recruitmentsystem.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/manage/roles")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @GetMapping("/all")
    public List<RoleDto> getAllRoles() {
        return roleService.findAllRoles();
    }

    @GetMapping("/find/{id}")
    public RoleDto getRoleById(@PathVariable("id") Integer id) {
        return roleService.findRoleResponseModelById(id);
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public void addRole(@RequestBody RoleDto request) {
        roleService.addRole(request);
    }

    @PostMapping("/update/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateRole(@PathVariable("id") Integer id,
                           @RequestBody RoleDto request) {
        roleService.updateRole(id, request);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRole(@PathVariable("id") Integer id) {
        roleService.deleteRole(id);
    }
}
