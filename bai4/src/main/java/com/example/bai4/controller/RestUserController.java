package com.example.bai4.controller;

import com.example.bai4.entity.User;
import com.example.bai4.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin("*") // Enable CORS for all origins
public class RestUserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        if (users == null || users.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        if (user == null) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "User not found with id: " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.ok(user);
    }

    @PostMapping("/users")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        try {
            userService.createUser(user);
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User user) {
        try {
            User existingUser = userService.getUserById(id);
            if (existingUser == null) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "User not found with id: " + id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            user.setId(id);
            userService.updateUser(user);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            User user = userService.getUserById(id);
            if (user == null) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "User not found with id: " + id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            userService.deleteUser(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "User deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}