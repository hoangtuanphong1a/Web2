package com.example.day4.security;

import com.example.day4.model.UserDemo;
import com.example.day4.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserDemo user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        // Kiểm tra null trước khi xử lý roles
        List<GrantedAuthority> authorities = (user.getRoles() != null) ?
                user.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + (role.getName() != null ? role.getName() : "USER")))
                        .collect(Collectors.toList()) :
                List.of();

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                authorities
        );
    }
}
