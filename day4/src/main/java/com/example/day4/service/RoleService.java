package com.example.day4.service;

import java.util.List;
import java.util.Optional;

import com.example.day4.model.Role;
import com.example.day4.repository.RoleRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    private final RoleRepository repository;

    public RoleService(RoleRepository repository) {
        this.repository = repository;
    }

    public List<Role> getAllRoles() {
        return this.repository.findAll();
    }

    public Optional<Role> getRoleById(long id) {
        return this.repository.findById(id);
    }

    public List<Role> getAllRolesByName(String name) {
        return this.repository.findByName(name);
    }

    public Role createRole(Role role) {
        return this.repository.save(role);
    }

    public void deleteRole(long id) {
        this.repository.deleteById(id);
    }
}