package com.example.day4.controller;

import com.example.day4.model.UserDemo;
import com.example.day4.service.UserService;
import com.example.day4.service.CompanyService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final CompanyService companyService;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    public UserController(UserService userService, CompanyService companyService) {
        this.userService = userService;
        this.companyService = companyService;
    }

    @GetMapping
    public String listUsers(Model model) {
        logger.info("listUsers called");
        try {
            List<UserDemo> users = userService.getAllUsers();
            logger.info("Found {} users", users.size());
            model.addAttribute("users", users);
            return "users";
        } catch (Exception e) {
            logger.error("Error getting users", e);
            model.addAttribute("error", "Error loading users.");
            return "error"; // Tạo một view "error.html" hoặc "error.jsp" để hiển thị lỗi.
        }
    }

    @GetMapping("/add")
    public String showAddUserForm(Model model) {
        logger.info("showAddUserForm called");
        model.addAttribute("user", new UserDemo());
        model.addAttribute("companies", companyService.getAllCompanies());
        return "add-user";
    }

    @PostMapping("/add")
    public String addUser(@ModelAttribute UserDemo user) {
        logger.info("addUser called with user: {}", user);
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            logger.warn("Password is required for adding user");
            return "redirect:/users/add?error=PasswordRequired";
        }
        try {
            userService.saveUser(user);
            logger.info("User added successfully: {}", user);
            return "redirect:/users";
        } catch (Exception e) {
            logger.error("Error adding user", e);
            return "redirect:/users/add?error=AddUserError";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditUserForm(@PathVariable Long id, Model model) {
        logger.info("showEditUserForm called with id: {}", id);
        Optional<UserDemo> user = userService.getUserById(id);
        if (user.isPresent()) {
            model.addAttribute("user", user.get());
            model.addAttribute("companies", companyService.getAllCompanies());
            return "edit-user";
        } else {
            logger.warn("User with id {} not found", id);
            return "redirect:/users?error=UserNotFound";
        }
    }

    @PostMapping("/edit")
    public String updateUser(@ModelAttribute UserDemo user) {
        logger.info("updateUser called with user: {}", user);
        try {
            userService.saveUser(user);
            logger.info("User updated successfully: {}", user);
            return "redirect:/users";
        } catch (Exception e) {
            logger.error("Error updating user", e);
            return "redirect:/users/edit/" + user.getId() + "?error=UpdateUserError";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        logger.info("deleteUser called with id: {}", id);
        try {
            userService.deleteUser(id);
            logger.info("User deleted successfully: {}", id);
            return "redirect:/users";
        } catch (Exception e) {
            logger.error("Error deleting user", e);
            return "redirect:/users?error=DeleteUserError";
        }
    }
}