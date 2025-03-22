package com.example.day6.repository;

import com.example.day6.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Integer> {
}
