package edu.uscb.csci470sp25.brighten_up_backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

@Entity
@Table(name = "user_ratings", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "post_id"})
})
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class UserRating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    @JsonIdentityReference(alwaysAsId = true)
    private AppUser user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false, foreignKey = @ForeignKey(name = "fk_rating_post", foreignKeyDefinition = "FOREIGN KEY (post_id) REFERENCES user_posts(id) ON DELETE CASCADE"))
    @JsonBackReference
    private UserPost post;

    @Column(nullable = false)
    private int rating;

    public UserRating() {}

    public UserRating(AppUser user, UserPost post, int rating) {
        this.user = user;
        this.post = post;
        this.rating = rating;
    }

    public Long getId() {
        return id;
    }

    public AppUser getUser() {
        return user;
    }

    public UserPost getPost() {
        return post;
    }

    public int getRating() {
        return rating;
    }

    public void setUser(AppUser user) {
        this.user = user;
    }

    public void setPost(UserPost post) {
        this.post = post;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}