package edu.uscb.csci470sp25.brighten_up_backend.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "app_users")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "role", nullable = false)
    private String role;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<UserPost> userPosts = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<UserRating> userRatings = new ArrayList<>();

    public AppUser() {}

    public AppUser(String email, String password, String role, String name, String username) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role != null ? role.toUpperCase() : "USER";
    }

    public List<UserPost> getUserPosts() {
        return userPosts;
    }

    public List<UserRating> getUserRatings() {
        return userRatings;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setUserPosts(List<UserPost> userPosts) {
        this.userPosts = userPosts;
    }

    public void setUserRatings(List<UserRating> userRatings) {
        this.userRatings = userRatings;
    }

    @Override
    public String toString() {
        return "AppUser{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}