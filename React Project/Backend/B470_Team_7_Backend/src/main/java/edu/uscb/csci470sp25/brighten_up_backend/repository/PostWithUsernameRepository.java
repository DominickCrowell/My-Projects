package edu.uscb.csci470sp25.brighten_up_backend.repository;

import edu.uscb.csci470sp25.brighten_up_backend.model.PostWithUsername;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PostWithUsernameRepository extends JpaRepository<PostWithUsername, Long> {
    Optional<PostWithUsername> findByPostId(Long postId);
}