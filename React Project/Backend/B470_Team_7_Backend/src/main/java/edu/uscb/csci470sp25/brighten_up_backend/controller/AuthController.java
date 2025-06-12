package edu.uscb.csci470sp25.brighten_up_backend.controller;

import edu.uscb.csci470sp25.brighten_up_backend.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public String register(@RequestBody AuthRequest authRequest) {
        return authService.registerUser(
        		authRequest.getEmail(), 
        		authRequest.getPassword(), 
        		authRequest.getRole(), 
        		authRequest.getUsername()
        );
    }

    // ✅ Login user
    @PostMapping("/login")
    public String login(@RequestBody AuthRequest authRequest) {
        return authService.authenticateUser(
            authRequest.getIdentifier(), // 👈 was getUsername()
            authRequest.getPassword()
        );
    }

}