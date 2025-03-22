package com.example.BankDemo.service;


import com.example.BankDemo.entity.User;
import com.example.BankDemo.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @BeforeEach()
    public void setUp() {
        userRepository = mock(UserRepository.class);
        customUserDetailsService = new CustomUserDetailsService(userRepository);
    }

    @Test
    public void testLoadUserByUsername_UserExists() {
        // Arrange
        String email = "test@example.com";
        User user = new User();
        user.setEmail(email);
        user.setPassword("password");

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // Act
        User loadedUser = (User) customUserDetailsService.loadUserByUsername(email);

        // Assert
        assertNotNull(loadedUser);
        assertEquals(email, loadedUser.getEmail());
    }

    @Test
    public void testLoadUserByUsername_UserNotFound() {
        // Arrange
        String email = "notfound@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act & Assert
        try {
            customUserDetailsService.loadUserByUsername(email);
            fail("Expected UsernameNotFoundException to be thrown");
        } catch (UsernameNotFoundException ex) {
            assertEquals("User not found: " + email, ex.getMessage());
        }
    }


    @Test
    public void testGetAllUsers() {
        // Arrange
        User user1 = new User();
        user1.setEmail("user1@example.com");
        User user2 = new User();
        user2.setEmail("user2@example.com");

        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        // Act
        List<User> users = customUserDetailsService.getAllUsers();

        // Assert
        assertEquals(2, users.size());
        assertEquals("user1@example.com", users.get(0).getEmail());
    }

    @Test
    public void testGetUserProfile_UserExists() {
        // Arrange
        String email = "profile@example.com";
        User user = new User();
        user.setEmail(email);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // Act
        Optional<User> result = customUserDetailsService.getUserProfile(authentication);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(email, result.get().getEmail());
    }

    @Test
    public void testUserExists_ReturnsTrue() {
        // Arrange
        String email = "exists@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(new User()));

        // Act
        boolean exists = customUserDetailsService.userExists(email);

        // Assert
        assertTrue(exists);
    }

    @Test
    public void testUserExists_ReturnsFalse() {
        // Arrange
        String email = "notexists@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act
        boolean exists = customUserDetailsService.userExists(email);

        // Assert
        assertFalse(exists);
    }
}
