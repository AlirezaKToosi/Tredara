package com.novare.tredara.repositories;

import com.novare.tredara.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest // This annotation configures the test environment for JPA repositories
class UserRepoTest {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private TestEntityManager entityManager; // Used for setting up test data

    @BeforeEach
    void Setup(){
        User user = new User("Alireza","Alireza@gmail.com","@Alireza123456@");
        entityManager.persistAndFlush(user);
    }

    @Test
    void findByEmail() {
        String email = "Alireza@gmail.com";
        Optional<User> userOptional = userRepo.findByEmail(email);
        assertEquals(email, userOptional.get().getEmail());
    }

    @Test
    void existsByEmail() {
        String existingEmail="Alireza@gmail.com";
        String nonExistingEmail = "nonexistent@example.com";
        assertTrue(userRepo.existsByEmail(existingEmail));
        assertFalse(userRepo.existsByEmail(nonExistingEmail));
    }

//    @Test
//    void findById() {
//        Optional<User> userOptional = userRepo.findById(1L);
//        assertEquals("Alireza@gmail.com", userOptional.get().getEmail());
//    }
}