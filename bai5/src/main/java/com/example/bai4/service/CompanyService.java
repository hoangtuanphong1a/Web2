package com.example.bai4.service;

import com.example.bai4.entity.Company;
import com.example.bai4.entity.User;
import com.example.bai4.repository.CompanyRepository;
import com.example.bai4.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyService {
    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    public List<Company> getCompaniesByUserId(Long userId) {
        return companyRepository.findByUserId(userId);
    }

    public Company getCompanyById(Long id) {
        return companyRepository.findById(id).orElse(null);
    }

    public void saveCompany(Company company) {
        companyRepository.save(company);
    }

    public void deleteCompany(Long id) {
        companyRepository.deleteById(id);
    }

    public void registerCompany(Company company, Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            company.setUser(user);
            companyRepository.save(company);
        }
    }
}