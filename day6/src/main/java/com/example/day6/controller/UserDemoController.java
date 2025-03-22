package com.example.day6.controller;

import com.example.day6.entity.Company;
import com.example.day6.entity.UserDemo;
import com.example.day6.service.CompanyService;
import com.example.day6.service.UserDemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserDemoController {

    @Autowired
    private UserDemoService userDemoService;

    @Autowired
    private CompanyService companyService;

    @GetMapping
    public List<UserDemo> getAllUsers() {
        return userDemoService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDemo> getUserById(@PathVariable Long id) {
        return userDemoService.getUserById(id)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public UserDemo createUser(
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("companyId") Integer companyId) {
        UserDemo user = new UserDemo();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(password);
        Company company = companyService.getCompanyById(companyId)
            .orElseThrow(() -> new RuntimeException("Company not found"));
        user.setCompany(company);
        return userDemoService.createUser(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDemo> updateUser(
            @PathVariable Long id,
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("companyId") Integer companyId) {
        UserDemo user = new UserDemo();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(password);
        Company company = companyService.getCompanyById(companyId)
            .orElseThrow(() -> new RuntimeException("Company not found"));
        user.setCompany(company);
        UserDemo updatedUser = userDemoService.updateUser(id, user);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userDemoService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
}