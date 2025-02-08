package com.example.BankDemo.controller;

import com.example.BankDemo.service.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/email")
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendEmail(@RequestBody Map<String, String> request) {
        String to = request.get("to");
        String subject = request.get("subject");
        String text = request.get("text");

        // Print entered email to console
        System.out.println("Entered Email: " + to);

        emailService.sendEmail(to, subject, text);
        return ResponseEntity.ok("Email sent successfully to " + to);
    }
}
