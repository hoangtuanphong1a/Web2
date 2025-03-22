package com.example.day3.controller;

import com.example.day3.model.UserDemo;
import com.example.day3.service.UserService;
import com.example.day3.service.CompanyService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final CompanyService companyService;

    public UserController(UserService userService, CompanyService companyService) {
        this.userService = userService;
        this.companyService = companyService;
    }

    @GetMapping
    public String listUsers(Model model) {
        List<UserDemo> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "users";
    }

    @GetMapping("/add")
    public String showAddUserForm(Model model) {
        model.addAttribute("user", new UserDemo());
        model.addAttribute("companies", companyService.getAllCompanies());
        return "add-user";
    }

    @PostMapping("/add")
    public String addUser(@ModelAttribute UserDemo user) {
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            return "redirect:/users/add?error=PasswordRequired";
        }
        userService.saveUser(user);
        return "redirect:/users";
    }

    @GetMapping("/edit/{id}")
    public String showEditUserForm(@PathVariable Long id, Model model) {
        Optional<UserDemo> user = userService.getUserById(id);
        if (user.isPresent()) {
            model.addAttribute("user", user.get());
            model.addAttribute("companies", companyService.getAllCompanies());
            return "edit-user";
        }
        return "redirect:/users";
    }

    @PostMapping("/edit")
    public String updateUser(@ModelAttribute UserDemo user) {
        userService.saveUser(user);
        return "redirect:/users";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/users";
    }
}
