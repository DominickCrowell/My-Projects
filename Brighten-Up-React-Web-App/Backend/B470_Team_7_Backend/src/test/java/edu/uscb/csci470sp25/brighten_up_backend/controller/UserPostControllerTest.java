package edu.uscb.csci470sp25.brighten_up_backend.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.jayway.jsonpath.JsonPath;

import edu.uscb.csci470sp25.brighten_up_backend.repository.AppUserRepository;
import edu.uscb.csci470sp25.brighten_up_backend.repository.UserPostRepository;
import edu.uscb.csci470sp25.brighten_up_backend.security.JwtUtil;
import io.jsonwebtoken.Claims;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserPostControllerTest {

    private static final Logger logger = LoggerFactory.getLogger(UserPostControllerTest.class);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private UserPostRepository userPostRepository;

    @Autowired
    private JwtUtil jwtUtil;

    private Long testUserId;
    private String jwtToken;

    @BeforeEach
    public void setup() throws Exception {
        // Configure MockMvc with Spring Security
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();

        // Clean the database
        userPostRepository.deleteAll();
        appUserRepository.deleteAll();
        logger.info("Database cleaned before test: Users={}, Posts={}",
                appUserRepository.count(), userPostRepository.count());

        // Register a test user
        String username = "johndoe";
        String email = "johndoe@example.com";
        String password = "password123";
        String role = "USER";
        String name = "John Doe";
        try {
            String registerPayload = String.format("{\"email\":\"%s\",\"password\":\"%s\",\"role\":\"%s\",\"name\":\"%s\",\"username\":\"%s\"}",
                    email, password, role, name, username);
            mockMvc.perform(post("/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(registerPayload))
                    .andExpect(status().isOk());
            logger.info("Test user registered with username: {}", username);
        } catch (Exception e) {
            logger.error("Failed to register user: {}", e.getMessage(), e);
            throw e;
        }

        // Authenticate via /auth/login endpoint
        try {
            String loginPayload = String.format("{\"identifier\":\"%s\",\"password\":\"%s\"}", username, password);
            String response = mockMvc.perform(post("/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(loginPayload))
                    .andExpect(status().isOk())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();
            jwtToken = response;
            logger.info("JWT token obtained via /auth/login: {}", jwtToken);
        } catch (Exception e) {
            logger.error("Failed to authenticate user: {}", e.getMessage(), e);
            throw e;
        }

        // Validate token manually
        try {
            Claims claims = jwtUtil.validateToken(jwtToken);
            logger.info("Token validated successfully. Claims: Subject={}, Email={}, Role={}, IssuedAt={}, ExpiresAt={}",
                    claims.getSubject(),
                    claims.get("email", String.class),
                    claims.get("role", String.class),
                    claims.getIssuedAt(),
                    claims.getExpiration());
        } catch (Exception e) {
            logger.error("Failed to validate JWT token: {}", e.getMessage(), e);
            throw e;
        }

        // Extract user ID
        try {
            testUserId = appUserRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Test user not found"))
                    .getId();
            logger.info("Test user ID: {}", testUserId);
        } catch (Exception e) {
            logger.error("Failed to retrieve user ID: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Test
    public void testCreateUserPost() throws Exception {
        String newPostJson = "{\"post\":\"Safe Post\"}";
        String authHeader = "Bearer " + jwtToken;
        logger.info("Testing POST /userposts with payload: {}, Authorization: {}", newPostJson, authHeader);
        try {
            String response = mockMvc.perform(post("/userposts")
                    .header("Authorization", authHeader)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(newPostJson))
                    .andReturn()
                    .getResponse()
                    .getContentAsString();
            int status = mockMvc.perform(post("/userposts")
                    .header("Authorization", authHeader)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(newPostJson))
                    .andReturn()
                    .getResponse()
                    .getStatus();
            logger.info("Create post response (status: {}): {}", status, response);

            mockMvc.perform(post("/userposts")
                    .header("Authorization", authHeader)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(newPostJson))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").exists())
                    .andExpect(jsonPath("$.user").value(testUserId.intValue()))
                    .andExpect(jsonPath("$.post").value("Safe Post"));
            logger.info("testCreateUserPost passed.");
        } catch (Exception e) {
            logger.error("Test failed: {}", e.getMessage(), e);
            throw e;
        }
    }
}