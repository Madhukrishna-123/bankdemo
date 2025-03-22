package com.example.BankDemo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import com.example.BankDemo.entity.User;
import com.example.BankDemo.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
    }
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserProfile(Authentication authentication) {
        String email = authentication.getName(); // Extract email from token
        return userRepository.findByEmail(email);
    }


    public boolean userExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
	

	
}
