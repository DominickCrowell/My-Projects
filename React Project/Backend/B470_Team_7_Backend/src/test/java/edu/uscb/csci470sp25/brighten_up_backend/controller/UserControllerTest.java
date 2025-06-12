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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.jayway.jsonpath.JsonPath;

import edu.uscb.csci470sp25.brighten_up_backend.repository.AppUserRepository;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserControllerTest {

    private static final Logger logger = LoggerFactory.getLogger(UserControllerTest.class);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private AppUserRepository appUserRepository;

    private Long testUserId;

    @BeforeEach
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

        // Clean the database before each test to ensure isolation
        appUserRepository.deleteAll();
        logger.info("Database cleaned before test.");

        // Insert a test user into the database
        String newUserJson = "{\"username\":\"johndoe\",\"email\":\"johndoe@example.com\",\"password\":\"password123\"}";
        String response = mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newUserJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Extract the ID from the response
        testUserId = ((Number) JsonPath.read(response, "$.id")).longValue();
        logger.info("Setup complete. Test user ID: {}", testUserId);
    }

    @Test
    public void testCreateUser() throws Exception {
        String newUserJson = "{\"username\":\"janedoe\",\"email\":\"janedoe@example.com\",\"password\":\"password123\"}";
        logger.info("Testing createUser with payload: {}", newUserJson);
        mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newUserJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("janedoe"))
                .andExpect(jsonPath("$.email").value("janedoe@example.com"))
                .andExpect(jsonPath("$.role").value("PRIVILEGED_USER")); // Updated role expectation
        logger.info("testCreateUser passed.");
    }

    @Test
    public void testGetAllUsers() throws Exception {
        logger.info("Testing getAllUsers");
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].username").value("johndoe"))
                .andExpect(jsonPath("$[0].email").value("johndoe@example.com"))
                .andExpect(jsonPath("$[0].role").value("PRIVILEGED_USER")); // Updated role expectation
        logger.info("testGetAllUsers passed.");
    }

    @Test
    public void testSearchUsers() throws Exception {
        logger.info("Testing searchUsers with query 'john'");
        mockMvc.perform(get("/users/search")
                .param("query", "john"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].username").value("johndoe"))
                .andExpect(jsonPath("$[0].email").value("johndoe@example.com"))
                .andExpect(jsonPath("$[0].role").value("PRIVILEGED_USER")); // Updated role expectation
        logger.info("testSearchUsers passed.");
    }

    @Test
    public void testSearchUsersNoResults() throws Exception {
        logger.info("Testing searchUsers with query 'nonexistent'");
        mockMvc.perform(get("/users/search")
                .param("query", "nonexistent"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isEmpty());
        logger.info("testSearchUsersNoResults passed.");
    }

    @Test
    public void testGetUserById() throws Exception {
        logger.info("Testing getUserById with ID: {}", testUserId);
        mockMvc.perform(get("/user/{id}", testUserId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testUserId.intValue()))
                .andExpect(jsonPath("$.username").value("johndoe"))
                .andExpect(jsonPath("$.email").value("johndoe@example.com"))
                .andExpect(jsonPath("$.role").value("PRIVILEGED_USER")); // Updated role expectation
        logger.info("testGetUserById passed.");
    }

    @Test
    public void testGetUserByIdNotFound() throws Exception {
        Long nonExistentId = 9999L;
        logger.info("Testing getUserById with non-existent ID: {}", nonExistentId);
        mockMvc.perform(get("/user/{id}", nonExistentId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Could not find the user with ID " + nonExistentId));
        logger.info("testGetUserByIdNotFound passed.");
    }

    @Test
    public void testDeleteUser() throws Exception {
        logger.info("Testing deleteUser with ID: {}", testUserId);
        mockMvc.perform(delete("/user/{id}", testUserId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Success! User with id " + testUserId + " has been deleted."))
                .andExpect(jsonPath("$.deletedUserId").value(testUserId.intValue()));

        // Verify the user is deleted
        mockMvc.perform(get("/user/{id}", testUserId))
                .andExpect(status().isNotFound());
        logger.info("testDeleteUser passed.");
    }

    @Test
    public void testDeleteUserNotFound() throws Exception {
        Long nonExistentId = 9999L;
        logger.info("Testing deleteUser with non-existent ID: {}", nonExistentId);
        mockMvc.perform(delete("/user/{id}", nonExistentId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Could not find the user with ID " + nonExistentId));
        logger.info("testDeleteUserNotFound passed.");
    }

    @Test
    public void testRegisterAdmin() throws Exception {
        String newAdminJson = "{\"username\":\"adminuser\",\"email\":\"admin@example.com\",\"password\":\"adminpass123\"}";
        logger.info("Testing registerAdmin with payload: {}", newAdminJson);
        mockMvc.perform(post("/admin/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newAdminJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Admin registered successfully"))
                .andExpect(jsonPath("$.username").value("adminuser"));

        // Verify the admin user is in the database
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.username=='adminuser')]").exists())
                .andExpect(jsonPath("$[?(@.username=='adminuser')].role").value("ADMIN"));
        logger.info("testRegisterAdmin passed.");
    }

    @Test
    public void testRegisterAdminEmailExists() throws Exception {
        String duplicateAdminJson = "{\"username\":\"newadmin\",\"email\":\"johndoe@example.com\",\"password\":\"adminpass123\"}";
        logger.info("Testing registerAdmin with duplicate email: {}", duplicateAdminJson);
        mockMvc.perform(post("/admin/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(duplicateAdminJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Email already exists"));
        logger.info("testRegisterAdminEmailExists passed.");
    }

    @Test
    public void testRegisterAdminUsernameExists() throws Exception {
        String duplicateAdminJson = "{\"username\":\"johndoe\",\"email\":\"newadmin@example.com\",\"password\":\"adminpass123\"}";
        logger.info("Testing registerAdmin with duplicate username: {}", duplicateAdminJson);
        mockMvc.perform(post("/admin/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(duplicateAdminJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Username already exists"));
        logger.info("testRegisterAdminUsernameExists passed.");
    }
}