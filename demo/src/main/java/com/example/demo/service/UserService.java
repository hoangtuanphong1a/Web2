package com.example.demo.service;

import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String registerUser(User user) {
        // Kiểm tra email đã tồn tại chưa
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return "Email đã tồn tại!";
        }

        // Mã hóa mật khẩu
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Tìm vai trò "USER", nếu chưa có thì tạo mới
        Role defaultRole = roleRepository.findByName("USER")
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setName("USER");
                    return roleRepository.save(newRole);
                });

        // Gán role cho user
        user.setRoles(Collections.singleton(defaultRole));

        // Lưu user vào database
        userRepository.save(user);

        return "User registered successfully!";
    }
}
