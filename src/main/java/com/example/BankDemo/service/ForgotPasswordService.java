package com.example.BankDemo.service;

import com.example.BankDemo.entity.User;
import com.example.BankDemo.repository.UserRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ForgotPasswordService {


	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final Map<String, String> otpStorage = new HashMap<>(); // Store OTPs temporarily
	private final JavaMailSender mailSender; // ✅ Add this


	public ForgotPasswordService(UserRepository userRepository, PasswordEncoder passwordEncoder, JavaMailSender mailSender) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.mailSender = mailSender;
	}


	public String sendOtp(String email) {
		Optional<User> userOptional = userRepository.findByEmail(email);

		if (userOptional.isEmpty()) {
			return "Invalid email! User not found.";
		}

		String otp = String.valueOf(new Random().nextInt(900000) + 100000);
		otpStorage.put(email, otp);
		System.out.println("Generated OTP for " + email + ": " + otp);
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(email);
		message.setSubject("Your OTP for Password Reset");
		message.setText("Hi, your OTP is: " + otp + " (valid for 5 minutes)");
		mailSender.send(message);

		return "OTP sent!";
	}

	public boolean verifyOtp(String email, String otp) {
		return otpStorage.containsKey(email) && otpStorage.get(email).equals(otp);
	}

	public boolean resetPassword(String email, String otp, String newPassword) {
		if (!verifyOtp(email, otp)) {
			return false;
		}

		Optional<User> userOptional = userRepository.findByEmail(email);
		if (userOptional.isEmpty()) {
			return false;
		}

		User user = userOptional.get();
		user.setPassword(passwordEncoder.encode(newPassword));
		userRepository.save(user);
		otpStorage.remove(email);
		return true;
	}
}

