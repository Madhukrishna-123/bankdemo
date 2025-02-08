package com.example.BankDemo.controller;

import com.example.BankDemo.entity.User;
import com.example.BankDemo.repository.UserRepository;
import com.example.BankDemo.service.CustomUserDetailsService;
import com.example.BankDemo.util.JwtUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
//@Controller
@CrossOrigin(origins = "http://127.0.0.1:5500")
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private CustomUserDetailsService customUserDetailsService;

	public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository,
			PasswordEncoder passwordEncoder) {
		this.authenticationManager = authenticationManager;
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@PostMapping("/save")
//    @ResponseBody
	public ResponseEntity<String> saveUser(@RequestBody User user) {
		String email = user.getEmail();
		if (customUserDetailsService.userExists(email)) {
            return ResponseEntity.badRequest().body("Email Already exists ...!");
        }
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		userRepository.save(user);
		return ResponseEntity.ok("User registered successfully");
	}

//    @PostMapping("/login")
//    public ResponseEntity<String> login(@RequestBody User user) {
//    	System.out.println(user.getEmail());
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
//        
//       
//
//        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//        
//       
//        return ResponseEntity.ok("Login successful for user: "  + userDetails.getUsername());
//    }

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody User user) {
		try {
			Authentication authentication = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));

			UserDetails userDetails = customUserDetailsService.loadUserByUsername(user.getEmail());
			String token = jwtUtil.generateToken(userDetails);
			System.out.println(token);
			return ResponseEntity.ok(token);
		} catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password"); // Return 401 for invalid login
		}
	}

	@GetMapping("/register")
	public String showRegisterPage() {
		return "register";
	}

	@GetMapping("/acclogin")
	public String showLoginPage() {
		return "login";
	}

	@GetMapping("/test")
    
    public String loginUser() {
        String url = "http://192.168.1.3:8080/school/login";

        RestTemplate restTemplate=new RestTemplate();
        // Creating request body
        Map<String, String> requestBody = new HashMap();
//        requestBody.put("email", "chinnu@gmail.com");
//        requestBody.put("password", "12345");
        requestBody.put("email", "teja123@gmail.com");
        requestBody.put("password", "teja123");

        // Setting headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        // Creating HTTP Entity with body and headers
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        // Making POST request
        ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.POST, requestEntity, String.class);

        return response.getBody();
    }
	

	@GetMapping("/pos")
    
    public String reg() {
        String url = "http://192.168.1.3:8080/school/register";

        RestTemplate restTemplate=new RestTemplate();
        // Creating request body
        Map<String, Object> requestBody = new HashMap();
//        requestBody.put("email", "chinnu@gmail.com");
//        requestBody.put("password", "12345");
        requestBody.put("email", "t@gmail.com");
        requestBody.put("password", "12345");
        requestBody.put("name", "MADHU");
        requestBody.put("roll_no", 23);

        // Setting headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        // Creating HTTP Entity with body and headers
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        // Making POST request
        ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.POST, requestEntity, String.class);

        return response.getBody();
    }
	
	
     @GetMapping("/ani")
    public String ani() {
        String url = "http://192.168.208.147:8080/auth/login";

        RestTemplate restTemplate=new RestTemplate();
        // Creating request body
        Map<String, String> requestBody = new HashMap();
//        requestBody.put("email", "chinnu@gmail.com");
//        requestBody.put("password", "12345");
        requestBody.put("email", "k@gmail.com");
        requestBody.put("password", "p@123");
//      

        // Setting headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        // Creating HTTP Entity with body and headers
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        // Making POST request
        ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.POST, requestEntity, String.class);

        return response.getBody();
    }

     @GetMapping("/all")
     public ResponseEntity<List<User>> getAllUsers() {
         List<User> users = customUserDetailsService.getAllUsers();
         return ResponseEntity.ok(users);
     }
     
     @GetMapping("/profile")
     public ResponseEntity<?> getUserProfile(Authentication authentication) {
         Optional<User> user = customUserDetailsService.getUserProfile(authentication);
         if (user.isPresent()) {
             return ResponseEntity.ok(user.get());
         } else {
             return ResponseEntity.status(404).body("User not found");
         }
     }

}
