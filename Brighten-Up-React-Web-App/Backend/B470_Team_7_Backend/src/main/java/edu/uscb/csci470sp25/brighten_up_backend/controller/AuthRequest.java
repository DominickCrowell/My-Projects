package edu.uscb.csci470sp25.brighten_up_backend.controller;

public class AuthRequest {
    private String email;
    private String password;
    private String role;
    private String username;
    private String identifier; 

    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
    public String getUsername() { return username; }
    public String getIdentifier() { return identifier; } 
}