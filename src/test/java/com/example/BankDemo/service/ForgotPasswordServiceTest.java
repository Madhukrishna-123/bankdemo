package com.example.BankDemo.service;

import com.example.BankDemo.entity.User;
import com.example.BankDemo.repository.UserRepository;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ForgotPasswordServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private ForgotPasswordService forgotPasswordService;

    private User testUser;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Initializes @Mock and @InjectMocks

        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");
        testUser.setPassword("oldPassword");
    }

    @Test
    public void testSendOtp_UserExists_ReturnsOtpSent() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));

        String result = forgotPasswordService.sendOtp("test@example.com");

        assertEquals("OTP sent!", result);
    }

    @Test
    public void testSendOtp_UserNotFound_ReturnsInvalidEmail() {
        when(userRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

        String result = forgotPasswordService.sendOtp("notfound@example.com");

        assertEquals("Invalid email! User not found.", result);
    }

    @Test
    public void testVerifyOtp_CorrectOtp_ReturnsTrue() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));
        forgotPasswordService.sendOtp("test@example.com");

        String otp = getStoredOtp("test@example.com");

        assertTrue(forgotPasswordService.verifyOtp("test@example.com", otp));
    }

    @Test
    public void testVerifyOtp_WrongOtp_ReturnsFalse() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));
        forgotPasswordService.sendOtp("test@example.com");

        assertFalse(forgotPasswordService.verifyOtp("test@example.com", "wrongOtp"));
    }

    @Test
    public void testResetPassword_Success() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedPassword");

        forgotPasswordService.sendOtp("test@example.com");
        String otp = getStoredOtp("test@example.com");

        boolean result = forgotPasswordService.resetPassword("test@example.com", otp, "newPassword");

        assertTrue(result);
        assertEquals("encodedPassword", testUser.getPassword());
        verify(userRepository).save(testUser);
    }

    @Test
    public void testResetPassword_WrongOtp_Failure() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));

        forgotPasswordService.sendOtp("test@example.com");

        boolean result = forgotPasswordService.resetPassword("test@example.com", "invalidOtp", "newPassword");

        assertFalse(result);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testResetPassword_UserNotFound_Failure() {
        when(userRepository.findByEmail("unknown@example.com")).thenReturn(Optional.empty());

        boolean result = forgotPasswordService.resetPassword("unknown@example.com", "otp", "password");

        assertFalse(result);
    }

    // Helper method to get OTP from private map using reflection
    private String getStoredOtp(String email) {
        try {
            java.lang.reflect.Field otpField = ForgotPasswordService.class.getDeclaredField("otpStorage");
            otpField.setAccessible(true);
            java.util.Map<String, String> otpMap = (java.util.Map<String, String>) otpField.get(forgotPasswordService);
            return otpMap.get(email);
        } catch (Exception e) {
            throw new RuntimeException("Failed to read OTP", e);
        }
    }
}
