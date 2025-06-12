package edu.uscb.csci470sp25.brighten_up_backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user_posts")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class UserPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_user_post", foreignKeyDefinition = "FOREIGN KEY (user_id) REFERENCES app_users(id) ON DELETE CASCADE"))
    @JsonBackReference
    @JsonIdentityReference(alwaysAsId = true)
    private AppUser user;

    @Column(unique = true, name = "post", nullable = false)
    private String post;

    @Column(name = "cumulativeRating")
    @JsonProperty("cumulative_rating")
    private Float cumulativeRating;

    @Column(name = "numOfRatings")
    @JsonProperty("num_of_ratings")
    private Long numOfRatings;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({"post"})
    private List<UserRating> ratings = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public AppUser getUser() {
        return user;
    }

    public String getPost() {
        return post;
    }

    public Float getCumulativeRating() {
        return cumulativeRating;
    }

    public Long getNumOfRatings() {
        return numOfRatings;
    }

    public List<UserRating> getRatings() {
        return ratings;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUser(AppUser user) {
        this.user = user;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public void setCumulativeRating(Float cumulativeRating) {
        this.cumulativeRating = cumulativeRating;
    }

    public void setNumOfRatings(Long numOfRatings) {
        this.numOfRatings = numOfRatings;
    }

    public void setRatings(List<UserRating> ratings) {
        this.ratings = ratings;
    }
}