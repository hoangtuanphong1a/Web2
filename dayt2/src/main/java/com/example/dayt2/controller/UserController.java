package com.example.dayt2.controller;
import com.example.dayt2.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {
    private List<User> users = new ArrayList<>();

    // GET: Hiển thị danh sách user
    @GetMapping("/")
    public String getUsers(Model model) {
        model.addAttribute("users", users);
        return "user-list";
    }

    // GET: Hiển thị form thêm user
    @GetMapping("/add-user")
    public String showAddUserForm(Model model) {
        model.addAttribute("user", new User());
        return "add-user";
    }

    // POST: Xử lý thêm user
    @PostMapping("/add-user")
    public String addUser(@ModelAttribute User user) {
        user.setId(users.size() + 1); // Tự động gán ID
        users.add(user);
        System.out.println("User added: " + user);
        return "redirect:/";
    }

    // GET: Hiển thị form sửa user
    @GetMapping("/edit-user/{id}")
    public String showEditUserForm(@PathVariable int id, Model model) {
        User user = users.stream()
                .filter(u -> u.getId() == id)
                .findFirst()
                .orElse(null);

        if (user == null) {
            throw new IllegalArgumentException("User not found with ID: " + id);
        }

        model.addAttribute("user", user);
        return "edit-user";
    }


    // POST: Xử lý sửa user
    @PostMapping("/edit-user")
    public String editUser(@ModelAttribute User updatedUser) {
        for (User user : users) {
            if (user.getId() == updatedUser.getId()) {
                user.setName(updatedUser.getName());
                user.setEmail(updatedUser.getEmail());
                System.out.println("User updated: " + updatedUser);
                break;
            }
        }
        return "redirect:/";
    }
}

