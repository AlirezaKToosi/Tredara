package com.novare.tredara.services;

import com.novare.tredara.models.User;
import com.novare.tredara.repositories.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepo userRepo;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void saveUser() {
        // Create a sample user
        User user = new User("Alireza", "Alireza@gmail.com", "@Alireza123456@");

        // Mock the behavior of the BCryptPasswordEncoder
        when(bCryptPasswordEncoder.encode("@Alireza123456@")).thenReturn("encodedPassword");

        // Mock the behavior of the UserRepo
        when(userRepo.save(user)).thenReturn(user);

        // Call the method being tested
        User savedUser = userService.saveUser(user);

        // Assertions
        assertNotNull(savedUser);
        assertEquals("encodedPassword", savedUser.getPassword());

    }

    @Test
    void findById() {
        when(userRepo.findById(1L)).thenReturn(Optional.of(new User("Alireza", "Alireza@gmail.com", "encodedPassword")));


        Optional<User> userOptional = userService.findById(1L);


        assertTrue(userOptional.isPresent());
        assertEquals("Alireza", userOptional.get().getFullName());
    }

    @Test
    void findByEmail() {
        // Mock the behavior of the UserRepo
        when(userRepo.findByEmail("Alireza@gmail.com")).thenReturn(Optional.of(new User("Alireza", "Alireza@gmail.com", "encodedPassword")));

        // Call the method being tested
        Optional<User> userOptional = userService.findByEmail("Alireza@gmail.com");

        // Assertions
        assertTrue(userOptional.isPresent());
        assertEquals("Alireza", userOptional.get().getFullName());
    }

    @Test
    void existsByEmail() {
        // Mock the behavior of the UserRepo
        when(userRepo.existsByEmail("Alireza@gmail.com")).thenReturn(true);

        // Call the method being tested
        boolean exists = userService.existsByEmail("Alireza@gmail.com");

        // Assertion
        assertTrue(exists);
    }

    @Test
    void isPasswordValid() {
        // Test a valid password
        assertTrue(userService.isPasswordValid("Password123!"));

        // Test an invalid password
        assertFalse(userService.isPasswordValid("weak"));
    }
}