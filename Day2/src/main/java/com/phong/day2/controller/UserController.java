package com.phong.day2.controller;


import com.phong.day2.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/")
    public String showForm(Model model) {
        model.addAttribute("user", new User());
        return "userform";
    }

    @PostMapping("/submit")
    public String submitForm(@ModelAttribute User user, Model model) {
        logger.info("Received user information: {}", user);
        model.addAttribute("message", "Thông tin đã được gửi thành công!");
        return "result";
    }
}