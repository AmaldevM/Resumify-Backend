package com.example.ResumeParser.Controller;

import com.example.ResumeParser.entity.User;
import com.example.ResumeParser.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    // Register endpoint
   @PostMapping("/register")
public ResponseEntity<?> registerUser(@RequestBody User user) {
    if (userRepository.findByEmail(user.getEmail()).isPresent()) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("User with this email already exists");
    }

    // Encode the password
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    User savedUser = userRepository.save(user);

    return ResponseEntity.ok(savedUser);
}


    // Login endpoint
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user) {
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            if (passwordEncoder.matches(user.getPassword(), existingUser.get().getPassword())) {
                return ResponseEntity.ok(existingUser.get()); // Password matched
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }
}
