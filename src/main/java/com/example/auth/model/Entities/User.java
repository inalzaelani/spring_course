package com.example.auth.model.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "user_tb")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "name", nullable = false)
    private String name;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "email", referencedColumnName = "email")
    private Auth auth;
    @Column(name = "phone")
    private String phone;

    public User() {
    }

    public User(Integer id, String name, Auth auth, String phone) {
        this.id = id;
        this.name = name;
        this.auth = auth;
        this.phone = phone;
    }
}
