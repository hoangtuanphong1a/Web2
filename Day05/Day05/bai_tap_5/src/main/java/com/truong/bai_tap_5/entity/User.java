package com.truong.bai_tap_5.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    public User() {
    }

    public User(Long id, String email, String password, Set<Role> roles) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }
    // Hàm hiển thị tên role
    public String getRoleNames() {
        return roles.stream().map(Role::getName).collect(Collectors.joining(", "));
    }
}
