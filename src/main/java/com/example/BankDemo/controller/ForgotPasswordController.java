package com.example.BankDemo.controller;


import com.example.BankDemo.service.CustomUserDetailsService;
import com.example.BankDemo.service.ForgotPasswordService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class ForgotPasswordController {

	@Autowired
	private CustomUserDetailsService customUserDetailsService;
    private final ForgotPasswordService forgotPasswordService;

    public ForgotPasswordController(ForgotPasswordService forgotPasswordService) {
        this.forgotPasswordService = forgotPasswordService;
    }

//    
//    @PostMapping("/forgot-password")
//    public ResponseEntity<String> sendOtp(@RequestBody Map<String, String> request) {
//        String email = request.get("email");
//        return ResponseEntity.ok(forgotPasswordService.sendOtp(email));
//    }
//
//    
//    @PostMapping("/verify-otp")
//    public ResponseEntity<String> verifyOtp(@RequestBody Map<String, String> request) {
//        String email = request.get("email");
//        String otp = request.get("otp");
//
//        boolean isValid = forgotPasswordService.verifyOtp(email, otp);
//        return isValid ? ResponseEntity.ok("OTP Verified!") : ResponseEntity.badRequest().body("Invalid OTP");
//    }
//
//   
//    @PostMapping("/reset-password")
//    public ResponseEntity<String> resetPassword(@RequestBody Map<String, String> request) {
//        String email = request.get("email");
//        String otp = request.get("otp");
//        String newPassword = request.get("newPassword");
//        System.out.println(newPassword);
//        return ResponseEntity.ok(forgotPasswordService.resetPassword(email,  newPassword));
//    }
////    @PostMapping("/reset-password")
////    public ResponseEntity<String> resetPassword(@RequestBody Map<String, String> request) {
////        String email = request.get("email");
////        String otp = request.get("otp");
////        String newPassword = request.get("newPassword");
////        System.out.println(newPassword);
////        return ResponseEntity.ok(forgotPasswordService.resetPassword(email, otp, newPassword));
////    }
    
    

    @PostMapping("/forgot-password")
    public ResponseEntity<String> sendOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");

        if (!customUserDetailsService.userExists(email)) {
            return ResponseEntity.badRequest().body("Invalid email! User not found.");
        }

        return ResponseEntity.ok(forgotPasswordService.sendOtp(email));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String otp = request.get("otp");

        boolean isValid = forgotPasswordService.verifyOtp(email, otp);
        return isValid ? ResponseEntity.ok("OTP Verified!") : ResponseEntity.badRequest().body("Invalid OTP");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String otp = request.get("otp");
        String newPassword = request.get("newPassword");

        boolean resetSuccessful = forgotPasswordService.resetPassword(email, otp, newPassword);
        return resetSuccessful ? ResponseEntity.ok("Password successfully reset!") : 
                                 ResponseEntity.badRequest().body("Invalid OTP or email!");
    }
}

