package com.example.day3.controller;

import com.example.day3.model.UserDemo;
import com.example.day3.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    // Lấy danh sách tất cả user
    @GetMapping
    public List<UserDemo> getAllUsers() {
        return userService.getAllUsers();
    }

    // Lấy thông tin user theo ID
    @GetMapping("/{id}")
    public UserDemo getUserById(@PathVariable Integer id) {
        return userService.findById(id);
    }

    // Tạo user mới
    @PostMapping
    public UserDemo createUser(@RequestBody UserDemo user) {
        return userService.saveUser(user);
    }

    // Cập nhật user
    @PutMapping("/{id}")
    public UserDemo updateUser(@PathVariable Integer id, @RequestBody UserDemo userDetails) {
        UserDemo user = userService.findById(id);
        if (user != null) {
            user.setFirstName(userDetails.getFirstName());
            user.setLastName(userDetails.getLastName());
            return userService.saveUser(user);
        }
        return null;
    }

    // Xóa user
    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return "User deleted successfully!";
    }
}

