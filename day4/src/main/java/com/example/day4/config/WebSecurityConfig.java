package com.example.day4.config;

import com.example.day4.security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Qualifier;
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
    public AuthenticationManager authenticationManager(
            @Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);

        return new ProviderManager(authenticationProvider);
    }

    // Loại bỏ định nghĩa InMemoryUserDetailsManager để tránh xung đột

    @Bean
    protected PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .headers(httpSecurityHeadersConfigurer ->
                        httpSecurityHeadersConfigurer.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/register", "/login").permitAll()
                        .requestMatchers("/users").hasAnyAuthority("USER", "ADMIN")
                        .anyRequest().permitAll())
                .formLogin(form -> form
                        .loginPage("/login").defaultSuccessUrl("/users")
                        .permitAll())
                .logout(config -> config.logoutSuccessUrl("/login"));
        return http.build();
    }
}
