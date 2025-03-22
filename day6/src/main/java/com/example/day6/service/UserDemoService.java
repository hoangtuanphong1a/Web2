package com.example.day6.service;

import com.example.day6.entity.*;
import com.example.day6.repository.UserDemoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserDemoService {
    
    @Autowired
    private UserDemoRepository userDemoRepository;
    
    // Create
    public UserDemo createUser(UserDemo user) {
        return userDemoRepository.save(user);
    }
    
    // Read all
    public List<UserDemo> getAllUsers() {
        return userDemoRepository.findAll();
    }
    
    // Read one
    public Optional<UserDemo> getUserById(Long id) {
        return userDemoRepository.findById(id);
    }
    
    // Update
    public UserDemo updateUser(Long id, UserDemo userDetails) {
        UserDemo user = userDemoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found"));
        user.setFirstName(userDetails.getFirstName());
        user.setLastName(userDetails.getLastName());
        user.setEmail(userDetails.getEmail());
        user.setPassword(userDetails.getPassword());
        user.setCompany(userDetails.getCompany());
        user.setRoles(userDetails.getRoles());
        return userDemoRepository.save(user);
    }
    
    // Delete
    public void deleteUser(Long id) {
        userDemoRepository.deleteById(id);
    }
}
