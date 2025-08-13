package com.skillforge.skillforge_api.controller;


import com.skillforge.skillforge_api.entity.Role;
import com.skillforge.skillforge_api.service.RoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PutMapping("/roles/{id}")
    public ResponseEntity<Role> updateRole(@PathVariable Long id,
                                           @RequestBody Role role) {
        Role updatedRole = roleService.handleUpdateRole(id, role);
        return ResponseEntity.ok(updatedRole);
    }
}
