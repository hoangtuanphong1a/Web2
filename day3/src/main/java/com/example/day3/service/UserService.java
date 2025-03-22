package com.example.day3.service;

import com.example.day3.model.UserDemo;
import com.example.day3.model.Role;
import com.example.day3.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    // Constructor injection
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserDemo> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<UserDemo> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public void saveUser(UserDemo user) {
        userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public void registerUser(UserDemo user, Role role) {
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email không được để trống!");
        }

        if (role == null) {
            throw new IllegalArgumentException("Role không tồn tại!");
        }

        user.getRoles().add(role); // Thêm role vào danh sách user
        userRepository.save(user);
    }
}
