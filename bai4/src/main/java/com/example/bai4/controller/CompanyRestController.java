package com.example.bai4.controller;

import com.example.bai4.entity.Company;
import com.example.bai4.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin("*")
public class CompanyRestController {

    @Autowired
    private CompanyService companyService;

    @GetMapping("/companies")
    public ResponseEntity<List<Company>> getAllCompanies() {
        List<Company> companies = companyService.getAllCompanies();
        if (companies.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(companies);
    }

    @GetMapping("/companies/{id}")
    public ResponseEntity<?> getCompanyById(@PathVariable Long id) {
        Company company = companyService.getCompanyById(id);
        if (company == null) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Company not found with id: " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.ok(company);
    }

    @PostMapping("/companies")
    public ResponseEntity<?> createCompany(@RequestBody Company company, @RequestParam Long userId) {
        try {
            companyService.registerCompany(company, userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(company);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/companies/{id}")
    public ResponseEntity<?> updateCompany(@PathVariable Long id, @RequestBody Company company, @RequestParam Long userId) {
        try {
            Company existingCompany = companyService.getCompanyById(id);
            if (existingCompany == null) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "Company not found with id: " + id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            company.setId(id);
            companyService.registerCompany(company, userId);
            return ResponseEntity.ok(company);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/companies/{id}")
    public ResponseEntity<?> deleteCompany(@PathVariable Long id) {
        try {
            Company company = companyService.getCompanyById(id);
            if (company == null) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "Company not found with id: " + id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            companyService.deleteCompany(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Company deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}