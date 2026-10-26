package com.example.Taskmaster;

import com.example.Taskmaster.model.User;
import com.example.Taskmaster.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void testSaveUserAndFindByUsername() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("hashedpassword");
        user.setEmailAddress("test@example.com");

        userRepository.save(user);

        Optional<User> foundUser = userRepository.findByUsername("testuser");
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmailAddress()).isEqualTo("test@example.com");
    }

    @Test
    void testExistsByUsername() {
        User user = new User();
        user.setUsername("existinguser");
        user.setPassword("pass");
        user.setEmailAddress("existing@example.com");
        userRepository.save(user);

        boolean exists = userRepository.existsByUsername("existinguser");
        boolean notExists = userRepository.existsByUsername("nonexistent");

        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }

    @Test
    void testExistsByEmailAddress() {
        User user = new User();
        user.setUsername("user2");
        user.setPassword("pass");
        user.setEmailAddress("duplicate@example.com");
        userRepository.save(user);

        boolean exists = userRepository.existsByEmailAddress("duplicate@example.com");
        boolean notExists = userRepository.existsByEmailAddress("unique@example.com");

        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }
}