package edu.uscb.csci470sp25.brighten_up_backend.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional; // Optional is used to avoid NullPointerExceptions

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import edu.uscb.csci470sp25.brighten_up_backend.model.AppUser; // Updated to AppUser

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class AppUserRepositoryTest { // Renamed to AppUserRepositoryTest

    @Autowired
    private AppUserRepository appUserRepository; // Updated to AppUserRepository

    @Test
    public void testFindById() {
        // Arrange
        AppUser appUser = new AppUser(); // Updated to AppUser
        appUser.setEmail("john.doe@example.com"); // Required field
        appUser.setPassword("password123"); // Required field
        appUser.setRole("USER"); // Required field
        appUser = appUserRepository.save(appUser);

        // Act
        Optional<AppUser> foundUser = appUserRepository.findById(appUser.getId());

        // Assert
        assertTrue(foundUser.isPresent());
    }

    @Test
    public void testSave() {
        // Arrange
        AppUser appUser = new AppUser(); // Updated to AppUser
        appUser.setEmail("jane.doe@example.com"); // Required field
        appUser.setPassword("password123"); // Required field
        appUser.setRole("USER"); // Required field

        // Act
        AppUser savedUser = appUserRepository.save(appUser);

        // Assert
        assertTrue(appUserRepository.findById(savedUser.getId()).isPresent());
    }

    @Test
    public void testDeleteById() {
        // Arrange
        AppUser appUser = new AppUser(); // Updated to AppUser
        appUser.setEmail("john.smith@example.com"); // Required field
        appUser.setPassword("password123"); // Required field
        appUser.setRole("USER"); // Required field
        appUser = appUserRepository.save(appUser);
        Long userId = appUser.getId();

        // Act
        appUserRepository.deleteById(userId);

        // Assert
        assertFalse(appUserRepository.findById(userId).isPresent());
    }
}