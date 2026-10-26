package com.example.Taskmaster.repository;

import com.example.Taskmaster.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    //usernames
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);

    //emails
    Optional<User> findByEmailAddress(String email);
    boolean existsByEmailAddress(String email);
}
