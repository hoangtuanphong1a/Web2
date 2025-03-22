package com.example.day4.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error, Model model) {
        logger.info("Login page requested");
        if (error != null) {
            logger.warn("Login failed: " + error);
            model.addAttribute("error", "Invalid username or password.");
        }
        return "login";
    }
}