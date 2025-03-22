package com.example.day6.entity;
import jakarta.persistence.*;

import java.util.List;

@Table(name = "COMPANY")
@Entity
public class Company {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column
    private String companyName;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "company_id")
    private List<UserDemo> users;

    public List<UserDemo> getUsers() {
        return users;
    }

    public void setUsers(List<UserDemo> users) {
        this.users = users;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getCompanyName() {
        return companyName;
    }
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}