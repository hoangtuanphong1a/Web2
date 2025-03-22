package com.example.bai4.config;

import com.example.bai4.service.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(authenticationProvider);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Định nghĩa bean PasswordEncoder
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .headers(httpSecurityHeadersConfigurer ->
                        httpSecurityHeadersConfigurer.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/register", "/login").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/api/v1/users").hasAnyAuthority("USER", "ADMIN")
                        .requestMatchers("/api/v1/users/**", "POST").hasAuthority("ADMIN")
                        .requestMatchers("/api/v1/users/**", "PUT").hasAuthority("ADMIN")
                        .requestMatchers("/api/v1/users/**", "DELETE").hasAuthority("ADMIN")
                        .requestMatchers("/users").hasAnyAuthority("USER", "ADMIN")
                        .requestMatchers("/users/add", "/users/edit/**", "/users/delete/**").hasAuthority("ADMIN")
                        .requestMatchers("/companies/**").hasAnyAuthority("USER", "ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login").defaultSuccessUrl("/users", true)
                        .permitAll())
                .logout(logout -> logout
                        .logoutSuccessUrl("/login").permitAll());
        return http.build();
    }
}