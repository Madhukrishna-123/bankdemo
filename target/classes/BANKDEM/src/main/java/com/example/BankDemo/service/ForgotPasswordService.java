package com.example.BankDemo.service;

import com.example.BankDemo.entity.User;
import com.example.BankDemo.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ForgotPasswordService {

//    private final UserRepository userRepository;
//    private final EmailService emailService;
//    private final PasswordEncoder passwordEncoder;
//
//   
//    private final ConcurrentHashMap<String, String> otpStorage = new ConcurrentHashMap<>();
//
//    public ForgotPasswordService(UserRepository userRepository, EmailService emailService, PasswordEncoder passwordEncoder) {
//        this.userRepository = userRepository;
//        this.emailService = emailService;
//        this.passwordEncoder = passwordEncoder;
//    }
//
//   
//    public String sendOtp(String email) {
//        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Email not found!"));
//
//        
//        String otp = String.format("%04d", new Random().nextInt(999999));
//
//       
//        otpStorage.put(email, otp);
//
//   
//        emailService.sendEmail(email, "Password Reset OTP", "Your OTP is: " + otp);
//
//        return "OTP sent successfully!";
//    }
//
// 
//    public boolean verifyOtp(String email, String otp) {
//    	if (!verifyOtp(email, otp)) {
//          throw new RuntimeException("Invalid OTP!");
//      }
//        String storedOtp = otpStorage.get(email);
//        if (storedOtp != null && storedOtp.equals(otp)) {
//            otpStorage.remove(email); 
//            return true;
//        }
//        return false;
//    }
//
//
////    public String resetPassword(String email, String otp, String newPassword) {
////        if (!verifyOtp(email, otp)) {
////            throw new RuntimeException("Invalid OTP!");
////        }
////
////        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found!"));
////        user.setPassword(passwordEncoder.encode(newPassword)); 
////        userRepository.save(user);
////
////        return "Password reset successfully!";
////    }
//    public String resetPassword(String email, String newPassword) {
////        if (!verifyOtp(email, otp)) {
////            throw new RuntimeException("Invalid OTP!");
////        }
//
//        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found!"));
//        user.setPassword(passwordEncoder.encode(newPassword)); 
//        userRepository.save(user);
//
//        return "Password reset successfully!";
//    }
	
	
	 private final UserRepository userRepository;
	    private final PasswordEncoder passwordEncoder;
	    private final Map<String, String> otpStorage = new HashMap<>(); // Store OTPs temporarily

	    public ForgotPasswordService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
	        this.userRepository = userRepository;
	        this.passwordEncoder = passwordEncoder;
	    }

	    // ✅ 1️⃣ Send OTP (Only if email exists)
	    public String sendOtp(String email) {
	        Optional<User> userOptional = userRepository.findByEmail(email);

	        if (userOptional.isEmpty()) {
	            return "Invalid email! User not found.";
	        }

	        String otp = String.valueOf(new Random().nextInt(900000) + 100000); // Generate 6-digit OTP
	        otpStorage.put(email, otp);
	        System.out.println("Generated OTP for " + email + ": " + otp); // Debug log

	        return "OTP sent!";
	    }

	    // ✅ 2️⃣ Verify OTP
	    public boolean verifyOtp(String email, String otp) {
	        return otpStorage.containsKey(email) && otpStorage.get(email).equals(otp);
	    }

	    // ✅ 3️⃣ Reset Password
	    public boolean resetPassword(String email, String otp, String newPassword) {
	        if (!verifyOtp(email, otp)) {
	            return false;
	        }

	        Optional<User> userOptional = userRepository.findByEmail(email);
	        if (userOptional.isEmpty()) {
	            return false;
	        }

	        User user = userOptional.get();
	        user.setPassword(passwordEncoder.encode(newPassword)); // Encode password
	        userRepository.save(user);
	        otpStorage.remove(email); // Remove OTP after successful reset
	        return true;
	    }
}
