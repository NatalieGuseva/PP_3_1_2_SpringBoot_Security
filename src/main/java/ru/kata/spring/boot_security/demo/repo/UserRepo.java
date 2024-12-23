package ru.kata.spring.boot_security.demo.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.models.User;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    User findByUsername(String username);

    User findByEmail(String email);
}
