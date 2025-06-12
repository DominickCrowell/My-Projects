package edu.uscb.csci470sp25.brighten_up_backend.service;

import edu.uscb.csci470sp25.brighten_up_backend.model.AppUser;
import edu.uscb.csci470sp25.brighten_up_backend.repository.AppUserRepository;
import edu.uscb.csci470sp25.brighten_up_backend.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Registers a new user by encoding their password and saving them to the database.
     */
    public String registerUser(String email, String password, String role, String username) {
        // Check if user already exists
        if (appUserRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("User already exists.");
        }

        // Encode password before saving user
        String hashedPassword = passwordEncoder.encode(password);
        // Force role to PRIVILEGED_USER, ignoring the provided role
        AppUser newUser = new AppUser();
        newUser.setEmail(email);
        newUser.setPassword(hashedPassword);
        newUser.setRole("PRIVILEGED_USER");
        newUser.setUsername(username);
        appUserRepository.save(newUser);

        return "User registered successfully";
    }

    /**
     * Authenticates a user by verifying their email and password, then generating a JWT token.
     */
    public String authenticateUser(String identifier, String password) {
        System.out.println("Attempting to authenticate user with identifier: " + identifier);
        Optional<AppUser> appUserOptional = appUserRepository.findByUsername(identifier);

        if (appUserOptional.isEmpty()) {
            appUserOptional = appUserRepository.findByEmail(identifier);
        }

        if (appUserOptional.isEmpty()) {
            System.out.println("User not found with identifier: " + identifier);
            throw new RuntimeException("Invalid username/email or password.");
        }

        AppUser appUser = appUserOptional.get();
        System.out.println("User found: " + appUser.getUsername() + ", role: " + appUser.getRole());

        if (!passwordEncoder.matches(password, appUser.getPassword())) {
            System.out.println("Password mismatch for user: " + appUser.getUsername());
            throw new RuntimeException("Invalid username/email or password.");
        }

        String token = jwtUtil.generateToken(appUser.getEmail(), appUser.getRole(), appUser.getId());
        System.out.println("Generated JWT token for user: " + appUser.getUsername());
        return token;
    }
}