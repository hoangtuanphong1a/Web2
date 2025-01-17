package com.phong.day2_2.controller;


import com.phong.day2_2.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    // Giả lập dữ liệu user (trong thực tế sẽ lấy từ database)
    private User demoUser = new User(1L, "Nguyễn Văn A", "nguyenvana@example.com", "0123456789", "Hà Nội");

    @GetMapping("/")
    public String showUserDetail(Model model) {
        model.addAttribute("user", demoUser);
        return "userDetail";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        // Trong thực tế sẽ tìm user từ database theo id
        model.addAttribute("user", demoUser);
        return "editForm";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable Long id, @ModelAttribute User user, Model model) {
        // Log thông tin cập nhật
        logger.info("Updating user information: {}", user);

        // Cập nhật thông tin user (trong thực tế sẽ lưu vào database)
        demoUser = user;
        demoUser.setId(id);

        model.addAttribute("user", demoUser);
        model.addAttribute("message", "Cập nhật thông tin thành công!");
        return "userDetail";
    }
}