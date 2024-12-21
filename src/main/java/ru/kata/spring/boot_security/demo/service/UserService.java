package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;

public interface UserService extends UserDetailsService {
    User findById(Long id);

    List<User> findAll();

    void save(User user);

    void delete(User user);

    void update(User user);

    User findByUsername(String username);

    User findByEmail(String email);
}


