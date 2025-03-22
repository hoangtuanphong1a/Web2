package com.example.day4.service;

import java.util.List;
import java.util.Optional;

import com.example.day4.model.Company;
import org.springframework.stereotype.Service;

import com.example.day4.repository.CompanyRepository;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public List<Company> getAllCompanies() {
        return this.companyRepository.findAll();
    }

    public Optional<Company> getCompanyById(long id) {
        return this.companyRepository.findById(id);
    }

    public List<Company> getAllCompaniesByName(String name) {
        return this.companyRepository.findByCompanyName(name);
    }

    public Company saveCompany(Company company) {
        return this.companyRepository.save(company);
    }

    public void deleteCompany(long id) {
        this.companyRepository.deleteById(id);
    }
}