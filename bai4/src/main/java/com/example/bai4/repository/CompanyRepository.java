package com.example.bai4.repository;

import com.example.bai4.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    // Thay findByUserId thành findByUsersId
    List<Company> findByUsersId(Long userId);
}