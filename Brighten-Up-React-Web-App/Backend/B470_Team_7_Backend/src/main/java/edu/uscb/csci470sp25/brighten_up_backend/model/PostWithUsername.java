package edu.uscb.csci470sp25.brighten_up_backend.model;

import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Synchronize;

import jakarta.persistence.*;

@Entity
@Immutable
@Subselect(
    "SELECT " +
    "    up.id AS post_id, " +
    "    up.user_id, " +
    "    au.username " +
    "FROM user_posts up " +
    "JOIN app_users au ON up.user_id = au.id"
)
@Synchronize({"user_posts", "app_users"})
public class PostWithUsername {

    @Id
    @Column(name = "post_id")
    private Long postId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "username")
    private String username;

    // Default constructor
    public PostWithUsername() {}

    // Getters and Setters
    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}