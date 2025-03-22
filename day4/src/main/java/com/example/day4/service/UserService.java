package com.example.day4.service;

import com.example.day4.model.Company;
import com.example.day4.model.Role;
import com.example.day4.model.UserDemo;
import com.example.day4.repository.CompanyRepository;
import com.example.day4.repository.RoleRepository;
import com.example.day4.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository, RoleRepository roleRepository,
                       CompanyRepository companyRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.companyRepository = companyRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserDemo> getAllUsers() {
        logger.info("Getting all users");
        try {
            return userRepository.findAll();
        } catch (Exception e) {
            logger.error("Error getting all users: {}", e.getMessage());
            throw new RuntimeException("Error getting all users", e);
        }
    }

    public Optional<UserDemo> getUserById(long id) {
        logger.info("Getting user by id: {}", id);
        try {
            return userRepository.findById(id);
        } catch (Exception e) {
            logger.error("Error getting user by id {}: {}", id, e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<UserDemo> getUserByEmail(String email) {
        logger.info("Getting user by email: {}", email);
        try {
            return userRepository.findByEmail(email).stream().findFirst();
        } catch (Exception e) {
            logger.error("Error getting user by email {}: {}", email, e.getMessage());
            return Optional.empty();
        }
    }

    public UserDemo saveUser(UserDemo user) {
        logger.info("Saving user: {}", user);
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            logger.error("Password cannot be empty");
            throw new IllegalArgumentException("Password cannot be empty");
        }

        // Kiểm tra nếu mật khẩu chưa được mã hóa thì mới mã hóa
        if (!user.getPassword().startsWith("$2a$")) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            logger.info("Password encoded for user: {}", user.getEmail());
        }

        try {
            UserDemo savedUser = userRepository.save(user);
            logger.info("User saved successfully: {}", user.getEmail());
            return savedUser;
        } catch (DataIntegrityViolationException e) {
            logger.error("Data integrity violation: {}", e.getMessage(), e);
            throw new RuntimeException("Email already exists or other data integrity issue", e);
        } catch (Exception e) {
            logger.error("Error saving user: {}", e.getMessage(), e); // Log chi tiết
            throw new RuntimeException("Error saving user", e);
        }
    }

    public void deleteUser(long id) {
        logger.info("Deleting user by id: {}", id);
        try {
            userRepository.deleteById(id);
            logger.info("User deleted successfully: {}", id);
        } catch (Exception e) {
            logger.error("Error deleting user by id {}: {}", id, e.getMessage());
            throw new RuntimeException("Error deleting user", e);
        }
    }

    public void assignRole(long userId, long roleId) {
        logger.info("Assigning role {} to user {}", roleId, userId);
        Optional<UserDemo> userOptional = userRepository.findById(userId);
        Optional<Role> roleOptional = roleRepository.findById(roleId);

        if (userOptional.isPresent() && roleOptional.isPresent()) {
            UserDemo user = userOptional.get();
            Role role = roleOptional.get();
            user.getRoles().add(role);
            userRepository.save(user);
            logger.info("Role assigned successfully: {} to user {}", roleId, userId);
        } else {
            logger.error("User or Role not found for assignment. User: {}, Role: {}", userId, roleId);
            throw new RuntimeException("User or Role not found for assignment");
        }
    }

    public void assignUserToCompany(long userId, long companyId) {
        logger.info("Assigning user {} to company {}", userId, companyId);
        Optional<UserDemo> userOptional = userRepository.findById(userId);
        Optional<Company> companyOptional = companyRepository.findById(companyId);

        if (userOptional.isPresent() && companyOptional.isPresent()) {
            UserDemo user = userOptional.get();
            Company company = companyOptional.get();
            user.setCompany(company);
            userRepository.save(user);
            logger.info("User assigned successfully to company: User {}, Company {}", userId, companyId);
        } else {
            logger.error("User or Company not found for assignment. User: {}, Company: {}", userId, companyId);
            throw new RuntimeException("User or Company not found for assignment");
        }
    }
}