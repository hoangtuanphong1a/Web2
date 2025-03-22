package com.example.day4.controller;

import com.example.day4.model.Role;
import com.example.day4.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/roles")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @GetMapping
    public List<Role> getAllRoles() {
        return roleService.getAllRoles();
    }

    @GetMapping("/{id}")
    public Optional<Role> getRoleById(@PathVariable Long id) {
        return roleService.getRoleById(id);
    }

    @PostMapping
    public Role createRole(@RequestBody Role role) {
        return roleService.createRole(role);
    }

    @DeleteMapping("/{id}")
    public String deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return "Role with ID " + id + " has been deleted.";
    }
}
