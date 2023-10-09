package com.novare.tredara.services;

import com.novare.tredara.models.User;
import com.novare.tredara.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    private String encodePassword(String password) {
        return bCryptPasswordEncoder.encode(password);
    }

    public User saveUser(User user) {
        user.setPassword(encodePassword(user.getPassword()));
        return userRepo.save(user);
    }
    public Optional<User> findById(Long id) {
        return userRepo.findById(id);
    }
    public Optional<User> findByEmail(String email) {
        return userRepo.findByEmail(email);
    }
    public boolean existsByEmail(String email) {
        return userRepo.existsByEmail(email);
    }
    public boolean isPasswordValid(String password) {
        // Define password validation criteria using regular expressions
        String lowerCaseRegex = ".*[a-z].*";
        String specialCharacterRegex = ".*[!@#$%^&*()_+={}.<>?].*";
        String upperCaseRegex = ".*[A-Z].*";

        // Check if the password matches all criteria
        return password != null
                && password.length()>= 8
                && password.matches(lowerCaseRegex)
                && password.matches(specialCharacterRegex)
                && password.matches(upperCaseRegex);
    }

}
