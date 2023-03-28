package com.example.auth.model.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "auth_tb")
public class Auth {
    @Id
    private String email;

    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "isActive", columnDefinition = "boolean default false")
    private Boolean isActive;

    public Auth() {
    }

    public Auth(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
