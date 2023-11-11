package com.recruitmentsystem.role;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/manage/roles")
@RequiredArgsConstructor
//@PreAuthorize("hasRole('ROLE_ADMIN')")
@PreAuthorize("hasAuthority('ADMIN')")
public class RoleController {
    private final RoleService roleService;

    @GetMapping("/all")
    public List<RoleResponseModel> getAllRoles() {
        return roleService.findAllRoles();
    }

    @GetMapping("/find/{id}")
    public RoleResponseModel getRoleById(@PathVariable("id") Integer id) {
        return roleService.findRoleResponseModelById(id);
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public void addRole(@RequestBody RoleRequestModel request) {
        roleService.addRole(request);
    }

    @PostMapping("/update/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateRole(@PathVariable("id") Integer id,
                                        @RequestBody RoleRequestModel request) {
        roleService.updateRole(id, request);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRole(@PathVariable("id") Integer id) {
        roleService.deleteRole(id);
    }
}
