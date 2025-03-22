package com.example.day4.controller;

import com.example.day4.model.UserDemo;
import com.example.day4.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RegisterController {

    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(RegisterController.class);

    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        logger.info("Register form requested");
        model.addAttribute("user", new UserDemo());
        return "register";
    }

    @PostMapping("/register")
    public String registerSubmit(@ModelAttribute UserDemo user, RedirectAttributes redirectAttributes) {
        logger.info("Register submit: {}", user);
        try {
            userService.saveUser(user);
            logger.info("User registered successfully: {}", user);
            redirectAttributes.addFlashAttribute("success", "Registration successful. Please login.");
            return "redirect:/login";
        } catch (Exception e) {
            logger.error("Error registering user: {}", e.getMessage(), e); // Log chi tiết
            redirectAttributes.addFlashAttribute("error", "Error registering user: " + e.getMessage());
            return "redirect:/register";
        }
    }
}