package com.example.BankDemo.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.BankDemo.entity.User;
import com.example.BankDemo.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    } 

    public void save(User user) {
        // Ensure password is hashed before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }
    
    
}
