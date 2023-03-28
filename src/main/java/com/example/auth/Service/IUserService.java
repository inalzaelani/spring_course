package com.example.auth.Service;

import com.example.auth.model.Entities.User;
import jakarta.persistence.criteria.CriteriaBuilder;

import java.util.List;
import java.util.Optional;

public interface IUserService {

    public List<User> findAll();

    Optional<User> findById(Integer id);

    User create(User user);

    void Update(User user);

    void delete(Integer id);
}
