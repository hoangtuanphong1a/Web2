package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, Model model) {
        String message = userService.registerUser(user);
        model.addAttribute("message", message);
        return "register";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @GetMapping("/users")
    public String listUsers(Model model, Authentication authentication) {
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        model.addAttribute("currentUser", authentication.getName());
        return "users";
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/login?logout";
    }
    
}
