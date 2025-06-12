package edu.uscb.csci470sp25.brighten_up_backend.repository;

import edu.uscb.csci470sp25.brighten_up_backend.model.UserPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserPostRepository extends JpaRepository<UserPost, Long> {
    // Check if a post with the same content exists (global)
    boolean existsByPost(String post);
    
    
    /*
    // Optional case-insensitive matching 
    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM UserPost p WHERE LOWER(p.post) = LOWER(:post)")
    boolean existsByPost(@Param("post") String post);
	*/
    List<UserPost> findByUserId(Long userId);
    
    /*
    // Optional duplicate post checked dependent on userId, not post alone 
    boolean existsByUserIdAndPost(Long userId, String post);
    */
}