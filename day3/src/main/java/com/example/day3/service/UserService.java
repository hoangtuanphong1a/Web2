package com.example.day3.service;

import com.example.day3.model.UserDemo;
import com.example.day3.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public UserDemo saveUser(UserDemo user) {
        return userRepository.save(user);
    }

    public List<UserDemo> getAllUsers() {
        return userRepository.findAll();
    }

    public UserDemo findById(Integer id) {
        Optional<UserDemo> user = userRepository.findById(id);
        return user.orElse(null);
    }

    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }
}
