package com.truong.bai_tap_5.repository;

import com.truong.bai_tap_5.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface  UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
