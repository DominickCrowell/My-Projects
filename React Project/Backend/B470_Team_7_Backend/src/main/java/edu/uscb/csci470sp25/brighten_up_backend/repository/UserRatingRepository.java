package edu.uscb.csci470sp25.brighten_up_backend.repository;

import edu.uscb.csci470sp25.brighten_up_backend.model.UserRating;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRatingRepository extends JpaRepository<UserRating, Long> {
    void deleteByPostId(Long postId);
    
    boolean existsByUserIdAndPostId(Long userId, Long postId);
    Optional<UserRating> findByUserIdAndPostId(Long userId, Long postId);

    
}
	