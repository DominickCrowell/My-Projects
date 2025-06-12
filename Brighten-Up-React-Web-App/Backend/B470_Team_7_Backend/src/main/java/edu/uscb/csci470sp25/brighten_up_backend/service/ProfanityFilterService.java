package edu.uscb.csci470sp25.brighten_up_backend.service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

@Service
public class ProfanityFilterService {
	
    private final Set<String> profaneWords = new HashSet<>();

    public ProfanityFilterService() {
        // Load profane words from the badwords.txt file
        try {
            // Load the file from src/main/resources/badwords.txt
            ClassPathResource resource = new ClassPathResource("data/badwords.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                // Trim whitespace and convert to lowercase for consistency
                String word = line.trim().toLowerCase();
                if (!word.isEmpty()) {
                    profaneWords.add(word);
                }
            }
            reader.close();
        } catch (Exception e) {
            throw new RuntimeException("Failed to load badwords.txt", e);
        }
    }

    public boolean hasProfanity(String text) {
        String[] words = text.toLowerCase().split("\\s+");
        for (String word : words) {
            if (profaneWords.contains(word)) {
                return true;
            }
        }
        return false;
    }

    public String filterProfanity(String text) {
        String[] words = text.split("\\s+");
        StringBuilder filtered = new StringBuilder();
        for (String word : words) {
            if (profaneWords.contains(word.toLowerCase())) {
                filtered.append("**** ");
            } else {
                filtered.append(word).append(" ");
            }
        }
        return filtered.toString().trim();
    }
   
}