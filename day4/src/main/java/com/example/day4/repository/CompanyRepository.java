package com.example.day4.repository;

import java.util.List;
import java.util.Optional;

import com.example.day4.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    List<Company> findByCompanyName(String companyName);
    Optional<Company> findById(long id);
}