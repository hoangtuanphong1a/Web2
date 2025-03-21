package com.example.bai4.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "companies") // Giữ nguyên tên bảng từ code gốc
public class Company {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO) // Đồng bộ với Company từ baitap3
    private Long id; // Thay int thành Long để đồng bộ với code gốc bai4

    @Column
    private String companyName; // Thay name thành companyName

    @OneToMany(cascade = CascadeType.ALL) // Sử dụng OneToMany thay vì ManyToOne như code gốc
    @JoinColumn(name = "company_id") // Cột company_id trong bảng users
    private List<User> users; // Thay UserDemo thành User

    // Constructors
    public Company() {
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}