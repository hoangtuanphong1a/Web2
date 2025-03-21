package com.example.bai4.service;

import com.example.bai4.entity.Role;
import com.example.bai4.entity.User;
import com.example.bai4.repository.RoleRepository;
import com.example.bai4.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String registerUser(User user, String roleName) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return "Email đã tồn tại!";
        }

        // Mã hóa mật khẩu
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Gán vai trò mặc định nếu không có role
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            Role userRole = getRole("USER");
            user.setRoles(Collections.singleton(userRole));
        }

        userRepository.save(user);
        return "User registered successfully!";
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public void updateUser(User user) {
        Optional<User> existingUserOptional = userRepository.findById(user.getId());

        if (existingUserOptional.isPresent()) {
            User existingUser = existingUserOptional.get();

            if (user.getFirstName() != null && !user.getFirstName().isEmpty()) {
                existingUser.setFirstName(user.getFirstName());
            }
            if (user.getLastName() != null && !user.getLastName().isEmpty()) {
                existingUser.setLastName(user.getLastName());
            }
            if (user.getEmail() != null && !user.getEmail().isEmpty()) {
                existingUser.setEmail(user.getEmail());
            }
            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            if (user.getRoles() != null && !user.getRoles().isEmpty()) {
                existingUser.setRoles(user.getRoles());
            }
            if (user.getCompany() != null) {
                existingUser.setCompany(user.getCompany());
            }

            userRepository.save(existingUser);
        }
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    private Role getRole(String roleName) {
        if (roleName == null || roleName.isEmpty()) {
            roleName = "USER";
        }
        String finalRoleName = roleName;
        return roleRepository.findByName(roleName).orElseGet(() -> createRole(finalRoleName));
    }

    private Role createRole(String roleName) {
        Role newRole = new Role();
        newRole.setName(roleName);
        return roleRepository.save(newRole);
    }
}