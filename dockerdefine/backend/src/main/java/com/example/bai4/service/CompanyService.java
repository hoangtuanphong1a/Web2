package com.example.bai4.service;

import com.example.bai4.entity.Company;
import com.example.bai4.entity.User;
import com.example.bai4.repository.CompanyRepository;
import com.example.bai4.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private UserRepository userRepository;

    public void registerCompany(Company company, Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        company.setUsers(Collections.singletonList(user));
        companyRepository.save(company);
    }

    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    public List<Company> getCompaniesByUserId(Long userId) {
        return companyRepository.findByUsersId(userId);
    }

    public Company getCompanyById(Long id) {
        return companyRepository.findById(id).orElse(null);
    }

    public void deleteCompany(Long id) {
        companyRepository.deleteById(id);
    }
}