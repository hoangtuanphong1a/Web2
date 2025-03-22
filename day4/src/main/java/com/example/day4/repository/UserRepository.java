package com.example.day4.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.day4.model.UserDemo;

@Repository
public interface UserRepository extends JpaRepository<UserDemo, Long> {
    Optional<UserDemo> findByEmail(String email); // Phải là Optional<UserDemo>
}
