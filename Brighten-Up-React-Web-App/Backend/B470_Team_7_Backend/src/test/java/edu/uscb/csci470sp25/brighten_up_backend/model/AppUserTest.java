package edu.uscb.csci470sp25.brighten_up_backend.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uscb.csci470sp25.brighten_up_backend.BrightenUpBackendApplication;
import edu.uscb.csci470sp25.brighten_up_backend.repository.AppUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = BrightenUpBackendApplication.class)
@Transactional
public class AppUserTest {

    @Autowired
    private AppUserRepository appUserRepository;

    @Test
    void testPersistAndGetId() {
        AppUser user = new AppUser();
        user.setEmail("test1@example.com");
        user.setPassword("password123"); // Ensure password is set
        user.setUsername("testuser1");
        user.setRole("admin");

        // Persist the user to the database
        AppUser savedUser = appUserRepository.save(user);

        // Verify that the ID is generated
        assertNotNull(savedUser.getId(), "ID should be generated after persisting");
        assertTrue(savedUser.getId() > 0, "ID should be a positive value");
        assertEquals("ADMIN", savedUser.getRole());
    }

    @Test
    void testJsonSerialization() throws Exception {
        AppUser user = new AppUser();
        user.setEmail("test2@example.com");
        user.setPassword("password123"); // Add missing password
        user.setUsername("testuser2");
        user.setRole("admin");

        // Persist the user to generate an ID
        AppUser savedUser = appUserRepository.save(user);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(savedUser);
        System.out.println("Serialized JSON: " + json);

        // Verify that userPosts and userRatings are excluded due to @JsonIgnore
        assertFalse(json.contains("userPosts"), "userPosts should be excluded from JSON");
        assertFalse(json.contains("userRatings"), "userRatings should be excluded from JSON");

        // Verify that other fields are present, including the generated ID
        assertTrue(json.contains("\"id\":" + savedUser.getId()));
        assertTrue(json.contains("\"email\":\"test2@example.com\""));
        assertTrue(json.contains("\"username\":\"testuser2\""));
        assertTrue(json.contains("\"role\":\"ADMIN\""));
    }
}