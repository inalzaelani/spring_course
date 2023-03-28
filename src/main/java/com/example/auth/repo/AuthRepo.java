package com.example.auth.repo;

import com.example.auth.model.Entities.Auth;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthRepo extends JpaRepository<Auth, String> {
}
