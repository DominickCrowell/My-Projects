package edu.uscb.csci470sp25.brighten_up_backend.model;

import edu.uscb.csci470sp25.brighten_up_backend.BrightenUpBackendApplication;
import edu.uscb.csci470sp25.brighten_up_backend.repository.AppUserRepository;
import edu.uscb.csci470sp25.brighten_up_backend.repository.PostWithUsernameRepository;
import edu.uscb.csci470sp25.brighten_up_backend.repository.UserPostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = BrightenUpBackendApplication.class)
@Transactional
public class PostWithUsernameTest {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private UserPostRepository userPostRepository;

    @Autowired
    private PostWithUsernameRepository postWithUsernameRepository;

    private AppUser user;
    private UserPost post;

    @BeforeEach
    void setUp() {
        // Create and save an AppUser
        user = new AppUser();
        user.setEmail("testuser@example.com");
        user.setPassword("password123");
        user.setUsername("testuser");
        user.setRole("USER");
        appUserRepository.save(user);

        // Create and save a UserPost associated with the user
        post = new UserPost();
        post.setUser(user);
        post.setPost("This is a test post 1"); // Use setPost instead of setContent, unique value
        post.setCumulativeRating(0.0f); // Optional field, set for clarity
        post.setNumOfRatings(0L); // Optional field, set for clarity
        userPostRepository.save(post);
    }

    @Test
    void testFindPostWithUsername() {
        // Query all PostWithUsername entries
        List<PostWithUsername> posts = postWithUsernameRepository.findAll();

        // Verify that the PostWithUsername projection works
        assertFalse(posts.isEmpty(), "There should be at least one PostWithUsername entry");
        PostWithUsername postWithUsername = posts.get(0);

        assertEquals(post.getId(), postWithUsername.getPostId(), "Post ID should match");
        assertEquals(user.getId(), postWithUsername.getUserId(), "User ID should match");
        assertEquals(user.getUsername(), postWithUsername.getUsername(), "Username should match");
    }

    @Test
    void testFindPostWithUsername_NoPosts() {
        // Delete all posts to test the case where there are no posts
        userPostRepository.deleteAll();

        // Query all PostWithUsername entries
        List<PostWithUsername> posts = postWithUsernameRepository.findAll();

        // Verify that no entries are returned
        assertTrue(posts.isEmpty(), "There should be no PostWithUsername entries when there are no posts");
    }

    @Test
    void testFindPostWithUsername_MultiplePosts() {
        // Add another post for the same user
        UserPost post2 = new UserPost();
        post2.setUser(user);
        post2.setPost("This is a test post 2"); // Unique post content to avoid constraint violation
        post2.setCumulativeRating(0.0f);
        post2.setNumOfRatings(0L);
        userPostRepository.save(post2);

        // Query all PostWithUsername entries
        List<PostWithUsername> posts = postWithUsernameRepository.findAll();

        // Verify that both posts are returned
        assertEquals(2, posts.size(), "There should be two PostWithUsername entries");

        // Verify the first post
        PostWithUsername postWithUsername1 = posts.stream()
            .filter(p -> p.getPostId().equals(post.getId()))
            .findFirst()
            .orElse(null);
        assertNotNull(postWithUsername1, "First post should be found");
        assertEquals(user.getId(), postWithUsername1.getUserId(), "User ID should match for first post");
        assertEquals(user.getUsername(), postWithUsername1.getUsername(), "Username should match for first post");

        // Verify the second post
        PostWithUsername postWithUsername2 = posts.stream()
            .filter(p -> p.getPostId().equals(post2.getId()))
            .findFirst()
            .orElse(null);
        assertNotNull(postWithUsername2, "Second post should be found");
        assertEquals(user.getId(), postWithUsername2.getUserId(), "User ID should match for second post");
        assertEquals(user.getUsername(), postWithUsername2.getUsername(), "Username should match for second post");
    }
}