package edu.uscb.csci470sp25.brighten_up_backend;

import edu.uscb.csci470sp25.brighten_up_backend.model.AppUser;
import edu.uscb.csci470sp25.brighten_up_backend.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class BrightenUpBackendApplication implements CommandLineRunner {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(BrightenUpBackendApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // Check if an admin user already exists
        if (appUserRepository.findByUsername("admin").isEmpty()) {
            AppUser admin = new AppUser();
            admin.setUsername("admin");
            admin.setEmail("admin@admin.com");
            admin.setPassword(passwordEncoder.encode("admin")); // Change this password in production
            admin.setRole("ADMIN");
            appUserRepository.save(admin);
            System.out.println("Default admin user created with username: admin and password: admin");
        } else {
            System.out.println("Admin user already exists, skipping default admin creation.");
        }
    }
}