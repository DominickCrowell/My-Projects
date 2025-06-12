package edu.uscb.csci470sp25.brighten_up_backend.repository;

import edu.uscb.csci470sp25.brighten_up_backend.model.AppUser;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByEmail(String email);
    Optional<AppUser> findByUsername(String username);
    List<AppUser> findByUsernameContainingIgnoreCase(String query, Sort sort);
}