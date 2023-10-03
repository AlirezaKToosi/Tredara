package com.novare.tredara.services;

import com.novare.tredara.models.User;
import com.novare.tredara.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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

    public boolean existsByEmail(String email) {
        return userRepo.existsByEmail(email);
    }

}
