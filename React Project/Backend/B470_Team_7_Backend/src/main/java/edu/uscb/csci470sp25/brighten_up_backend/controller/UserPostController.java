package edu.uscb.csci470sp25.brighten_up_backend.controller;

import edu.uscb.csci470sp25.brighten_up_backend.dto.RatingRequest;
import edu.uscb.csci470sp25.brighten_up_backend.model.AppUser;
import edu.uscb.csci470sp25.brighten_up_backend.model.UserPost;
import edu.uscb.csci470sp25.brighten_up_backend.model.UserRating;
import edu.uscb.csci470sp25.brighten_up_backend.model.PostWithUsername;
import edu.uscb.csci470sp25.brighten_up_backend.repository.AppUserRepository;
import edu.uscb.csci470sp25.brighten_up_backend.repository.UserPostRepository;
import edu.uscb.csci470sp25.brighten_up_backend.repository.UserRatingRepository;
import edu.uscb.csci470sp25.brighten_up_backend.repository.PostWithUsernameRepository;
import edu.uscb.csci470sp25.brighten_up_backend.security.CustomUserDetails;
import edu.uscb.csci470sp25.brighten_up_backend.service.ProfanityFilterService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/userposts")
public class UserPostController {

	private static final Logger logger = LoggerFactory.getLogger(UserPostController.class);
	
    @Autowired
    private UserPostRepository userPostRepository;

    @Autowired
    private UserRatingRepository userRatingRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private PostWithUsernameRepository postWithUsernameRepository;

    @Autowired
    private ProfanityFilterService profanityFilterService;

    // 🔍 GET all posts
    @GetMapping
    public List<UserPost> getAllPosts() {
        return userPostRepository.findAll();
    }

    // 🔍 GET a single post by ID
    @GetMapping("/{postId}")
    public ResponseEntity<UserPost> getPostById(@PathVariable Long postId) {
        UserPost post = userPostRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + postId));
        return ResponseEntity.ok(post);
    }

    // 🔍 GET username by post ID (new endpoint)
    @GetMapping("/{postId}/username")
    public ResponseEntity<String> getUsernameByPostId(@PathVariable Long postId) {
        PostWithUsername postWithUsername = postWithUsernameRepository.findByPostId(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + postId));
        return ResponseEntity.ok(postWithUsername.getUsername());
    }

    // 🔍 GET posts by a specific user
    @GetMapping("/by-user/{userId}")
    public List<UserPost> getPostsByUserId(@PathVariable Long userId) {
        return userPostRepository.findByUserId(userId);
    }

    // ✅ POST a new quote
    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody UserPost userPost, Authentication authentication) {
        String postContent = userPost.getPost().trim();
        if (profanityFilterService.hasProfanity(postContent)) {
            return ResponseEntity.badRequest().body("Post contains inappropriate language and cannot be submitted.");
        }
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or missing authentication.");
        }
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getUser().getId();
        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        boolean postExists = userPostRepository.existsByPost(postContent);
        if (postExists) {
            return ResponseEntity.badRequest().body("Duplicate post detected. This post has already been submitted.");
        }
        try {
            userPost.setUser(user);
            userPost.setPost(postContent);
            // Initialize cumulativeRating and numOfRatings
            userPost.setCumulativeRating(0.0f);
            userPost.setNumOfRatings(0L);
            UserPost savedPost = userPostRepository.save(userPost);

            // Automatically create a 5-star rating for the user's own post
            UserRating rating = new UserRating();
            rating.setPost(savedPost);
            rating.setUser(user);
            rating.setRating(5);
            userRatingRepository.save(rating);

            // Update cumulative rating and number of ratings
            savedPost.setNumOfRatings(1L);
            savedPost.setCumulativeRating(5.0f);
            userPostRepository.save(savedPost);

            return ResponseEntity.ok(savedPost);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().body("Duplicate post detected. This post has already been submitted.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to create post: " + e.getMessage());
        }
    }
    
    // 🗑 DELETE a single post + its ratings
    @Transactional
    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable Long postId) {
        userRatingRepository.deleteByPostId(postId);
        userPostRepository.deleteById(postId);
        return ResponseEntity.ok().build();
    }

    // 🗑 DELETE multiple posts by their IDs
    @Transactional
    @DeleteMapping("/bulk")
    public ResponseEntity<?> deletePosts(@RequestBody List<Long> postIds) {
        if (postIds == null || postIds.isEmpty()) {
            return ResponseEntity.badRequest().body("No post IDs provided for deletion.");
        }
        try {
            for (Long postId : postIds) {
                userRatingRepository.deleteByPostId(postId);
                userPostRepository.deleteById(postId);
            }
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to delete posts: " + e.getMessage());
        }
    }

    // 🗑 DELETE all posts for a user
    @Transactional
    @DeleteMapping("/by-user/{userId}")
    public ResponseEntity<?> deleteAllPostsByUserId(@PathVariable Long userId) {
        List<UserPost> posts = userPostRepository.findByUserId(userId);
        for (UserPost post : posts) {
            userRatingRepository.deleteByPostId(post.getId());
            userPostRepository.deleteById(post.getId());
        }
        return ResponseEntity.ok().build();
    }

    // ⭐ POST a rating for a post
    @PostMapping("/{postId}/rate")
    public ResponseEntity<?> ratePost(@PathVariable Long postId,
                                      @RequestBody RatingRequest ratingRequest,
                                      Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getUser().getId();
        UserPost post = userPostRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        boolean alreadyRated = userRatingRepository.existsByUserIdAndPostId(userId, postId);
        if (alreadyRated) {
            return ResponseEntity.badRequest().body("You have already rated this post.");
        }
        UserRating rating = new UserRating();
        rating.setPost(post);
        rating.setUser(userDetails.getUser());
        rating.setRating(ratingRequest.getRating());
        userRatingRepository.save(rating);
        long newNumRatings = post.getNumOfRatings() == null ? 1 : post.getNumOfRatings() + 1;
        float newCumulative = post.getCumulativeRating() == null
                ? ratingRequest.getRating()
                : post.getCumulativeRating() + ratingRequest.getRating();
        post.setNumOfRatings(newNumRatings);
        post.setCumulativeRating(newCumulative);
        userPostRepository.save(post);
        return ResponseEntity.ok(post);
    }

    // 👤 GET current user's rating for a post
    @GetMapping("/{postId}/my-rating")
    public ResponseEntity<?> getMyRating(@PathVariable Long postId, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getUser().getId();
        return userRatingRepository.findByUserIdAndPostId(userId, postId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    // Handle RuntimeException locally within this controller
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException ex) {
        logger.error("RuntimeException: {}", ex.getMessage());
        Map<String, Object> response = new HashMap<>();
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}