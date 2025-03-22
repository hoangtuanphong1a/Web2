package com.example.bai4.service;

import com.example.bai4.entity.Role;
import com.example.bai4.entity.User;
import com.example.bai4.repository.RoleRepository;
import com.example.bai4.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String registerUser(User user, String roleName) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return "Email đã tồn tại!";
        }
        if (!user.getPassword().startsWith("$2a$")) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            Role userRole = getRole(roleName);
            user.setRoles(Collections.singleton(userRole));
        }
        userRepository.save(user);
        return "User registered successfully!";
    }

    public void createUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email đã tồn tại!");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            Role defaultRole = getRole("USER");
            user.setRoles(Collections.singleton(defaultRole));
        }
        userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User not found!");
        }
        userRepository.deleteById(id);
    }

    public void updateUser(User user) {
        Optional<User> existingUserOptional = userRepository.findById(user.getId());
        if (existingUserOptional.isEmpty()) {
            throw new IllegalArgumentException("User not found!");
        }
        User existingUser = existingUserOptional.get();
        if (user.getEmail() != null && !user.getEmail().isEmpty()) {
            existingUser.setEmail(user.getEmail());
        }
        if (user.getPassword() != null && !user.getPassword().isEmpty() && !user.getPassword().startsWith("$2a$")) {
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            existingUser.setRoles(user.getRoles());
        }
        if (user.getCompany() != null) {
            existingUser.setCompany(user.getCompany());
        }
        userRepository.save(existingUser);
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    private Role getRole(String roleName) {
        if (roleName == null || roleName.isEmpty()) {
            roleName = "USER";
        }
        String finalRoleName = roleName;
        return roleRepository.findByName(roleName).orElseGet(() -> createRole(finalRoleName));
    }

    private Role createRole(String roleName) {
        Role newRole = new Role();
        newRole.setName(roleName);
        return roleRepository.save(newRole);
    }
}