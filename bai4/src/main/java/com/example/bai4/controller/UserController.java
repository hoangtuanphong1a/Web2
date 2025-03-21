package com.example.bai4.controller;

import com.example.bai4.entity.Role;
import com.example.bai4.entity.User;
import com.example.bai4.entity.AuthRequest;
import com.example.bai4.security.JwtService;
import com.example.bai4.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    // Đăng ký user mới
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user,
                                               @RequestParam(value = "roleIds", required = false) List<Long> roleIds) {
        Set<Role> roles = new HashSet<>();
        if (roleIds != null) {
            for (Long roleId : roleIds) {
                Role role = userService.getAllRoles().stream()
                        .filter(r -> r.getId().equals(roleId))
                        .findFirst().orElse(null);
                if (role != null) {
                    roles.add(role);
                }
            }
        }
        user.setRoles(roles);
        String result = userService.registerUser(user, null);
        if (result.contains("successfully")) {
            return ResponseEntity.status(201).body(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    // Tạo token sau khi đăng nhập
    @PostMapping("/generateToken")
    public ResponseEntity<String> authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getUsername(),
                        authRequest.getPassword()
                )
        );
        if (authentication.isAuthenticated()) {
            String token = jwtService.generateToken(authRequest.getUsername());
            return ResponseEntity.ok(token);
        } else {
            throw new UsernameNotFoundException("Invalid user request!");
        }
    }

    // Create a new user (yêu cầu quyền ADMIN)
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user,
                                           @RequestParam(value = "roleIds", required = false) List<Long> roleIds) {
        Set<Role> roles = new HashSet<>();
        if (roleIds != null) {
            for (Long roleId : roleIds) {
                Role role = userService.getAllRoles().stream()
                        .filter(r -> r.getId().equals(roleId))
                        .findFirst().orElse(null);
                if (role != null) {
                    roles.add(role);
                }
            }
        }
        user.setRoles(roles);
        userService.registerUser(user, null);
        return ResponseEntity.status(201).body(user);
    }

    // Read all users
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // Read a single user by ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    // Update an existing user
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id,
                                           @RequestBody User user,
                                           @RequestParam(value = "roleIds", required = false) List<Long> roleIds) {
        User existingUser = userService.getUserById(id);
        if (existingUser == null) {
            return ResponseEntity.notFound().build();
        }
        user.setId(id);
        Set<Role> roles = new HashSet<>();
        if (roleIds != null) {
            for (Long roleId : roleIds) {
                Role role = userService.getAllRoles().stream()
                        .filter(r -> r.getId().equals(roleId))
                        .findFirst().orElse(null);
                if (role != null) {
                    roles.add(role);
                }
            }
        }
        user.setRoles(roles);
        userService.updateUser(user);
        return ResponseEntity.ok(user);
    }

    // Delete a user
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        User user = userService.getUserById(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}