package com.example.day6.service;

import com.example.day6.entity.*;
import com.example.day6.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {
    
    @Autowired
    private CompanyRepository companyRepository;
    
    // Create
    public Company createCompany(Company company) {
        return companyRepository.save(company);
    }
    
    // Read all
    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }
    
    // Read one
    public Optional<Company> getCompanyById(Integer id) {
        return companyRepository.findById(id);
    }
    
    // Update
    public Company updateCompany(Integer id, Company companyDetails) {
        Company company = companyRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Company not found"));
        company.setCompanyName(companyDetails.getCompanyName());
        company.setUsers(companyDetails.getUsers());
        return companyRepository.save(company);
    }
    
    // Delete
    public void deleteCompany(Integer id) {
        companyRepository.deleteById(id);
    }
}
