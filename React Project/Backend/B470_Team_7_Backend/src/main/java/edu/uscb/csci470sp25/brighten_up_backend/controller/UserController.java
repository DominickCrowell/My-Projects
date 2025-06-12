package edu.uscb.csci470sp25.brighten_up_backend.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import edu.uscb.csci470sp25.brighten_up_backend.exception.UserNotFoundException;
import edu.uscb.csci470sp25.brighten_up_backend.model.AppUser;
import edu.uscb.csci470sp25.brighten_up_backend.repository.AppUserRepository;

@RestController
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/user")
    AppUser newUser(@RequestBody AppUser newUser) {
        // Set default role to PRIVILEGED_USER and encode password
        newUser.setRole("PRIVILEGED_USER");
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        return appUserRepository.save(newUser);
    }

    @PostMapping("/admin/register")
    public ResponseEntity<Map<String, Object>> registerAdmin(@RequestBody AppUser newAdmin) {
        // Check if user already exists
        if (appUserRepository.findByEmail(newAdmin.getEmail()).isPresent()) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Email already exists");
            return ResponseEntity.badRequest().body(response);
        }
        if (appUserRepository.findByUsername(newAdmin.getUsername()).isPresent()) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Username already exists");
            return ResponseEntity.badRequest().body(response);
        }

        // Set role to ADMIN and encode password
        newAdmin.setRole("ADMIN");
        newAdmin.setPassword(passwordEncoder.encode(newAdmin.getPassword()));
        appUserRepository.save(newAdmin);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Admin registered successfully");
        response.put("username", newAdmin.getUsername());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/users")
    List<AppUser> getAllUsers() {
        return appUserRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    @GetMapping("/users/search")
    List<AppUser> searchUsers(@RequestParam String query) {
        logger.info("🔍 Searching users with query: {}", query);
        return appUserRepository.findByUsernameContainingIgnoreCase(query, Sort.by(Sort.Direction.ASC, "id"));
    }
    
    @GetMapping("/user/{id}")
    AppUser getUserById(@PathVariable Long id) {
        return appUserRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Transactional
    @DeleteMapping("/user/{userId}")
    public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable Long userId) {
        logger.info("🔥 DELETE /user/{} endpoint hit", userId);

        if (!appUserRepository.existsById(userId)) {
            logger.warn("User with ID {} not found", userId);
            throw new UserNotFoundException(userId);
        }

        try {
            appUserRepository.deleteById(userId);
            logger.info("User with ID {} deleted successfully along with associated posts and ratings", userId);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Success! User with id " + userId + " has been deleted.");
            response.put("deletedUserId", userId);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Failed to delete user with ID {}: {}", userId, e.getMessage());
            throw new RuntimeException("Failed to delete user: " + e.getMessage());
        }
    }
    
    // Handle UserNotFoundException locally within this controller
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleUserNotFoundException(UserNotFoundException ex) {
        logger.error("UserNotFoundException: {}", ex.getMessage());
        Map<String, Object> response = new HashMap<>();
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}