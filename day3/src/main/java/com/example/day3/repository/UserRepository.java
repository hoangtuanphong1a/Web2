package com.example.day3.repository;

import com.example.day3.model.UserDemo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserDemo, Long> {
}
