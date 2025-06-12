package edu.uscb.csci470sp25.brighten_up_backend.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import edu.uscb.csci470sp25.brighten_up_backend.BrightenUpBackendApplication;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = BrightenUpBackendApplication.class)
public class ProfanityFilterServiceTest {

    private ProfanityFilterService profanityFilterService;

    @BeforeEach
    void setUp() {
        profanityFilterService = new ProfanityFilterService();
    }

    @Test
    void testHasProfanityWithSingleWord() {
        // Test single word profanity (assumes "damn" and "fuck" are in badwords.txt)
        assertTrue(profanityFilterService.hasProfanity("This is damn code"));
        assertTrue(profanityFilterService.hasProfanity("What the fuck"));
        assertFalse(profanityFilterService.hasProfanity("This is clean code"));
    }

    @Test
    void testHasProfanityWithMultiWordPhrase() {
        // Test multi-word phrase (only "bitch" will be detected, assuming it's in badwords.txt)
        assertTrue(profanityFilterService.hasProfanity("You son of a bitch")); // "bitch" should be in badwords.txt
        assertTrue(profanityFilterService.hasProfanity("Fuck you, buddy")); // "fuck" should be in badwords.txt
        assertFalse(profanityFilterService.hasProfanity("You son of a dog"));
    }

    @Test
    void testHasProfanityCaseInsensitivity() {
        // Test case insensitivity
        assertTrue(profanityFilterService.hasProfanity("This is DAMN code"));
        assertTrue(profanityFilterService.hasProfanity("FUCK YOU"));
        assertTrue(profanityFilterService.hasProfanity("Son Of A Bitch")); // "bitch" should be in badwords.txt
    }

    @Test
    void testFilterProfanityWithSingleWord() {
        // Test filtering single word profanity
        assertEquals("This is **** code", profanityFilterService.filterProfanity("This is damn code"));
        assertEquals("What the ****", profanityFilterService.filterProfanity("What the fuck"));
        assertEquals("This is clean code", profanityFilterService.filterProfanity("This is clean code"));
    }

    @Test
    void testFilterProfanityWithMultiWordPhrase() {
        // Test filtering multi-word phrase (each profane word replaced with "****")
        assertEquals("You son of a ****", profanityFilterService.filterProfanity("You son of a bitch"));
        assertEquals("**** you, buddy", profanityFilterService.filterProfanity("Fuck you, buddy"));
        assertEquals("You son of a dog", profanityFilterService.filterProfanity("You son of a dog"));
    }

    @Test
    void testFilterProfanityCaseInsensitivity() {
        // Test case insensitivity in filtering
        assertEquals("This is **** code", profanityFilterService.filterProfanity("This is DAMN code"));
        assertEquals("**** YOU", profanityFilterService.filterProfanity("FUCK YOU"));
        assertEquals("Son Of A ****", profanityFilterService.filterProfanity("Son Of A Bitch"));
    }

    @Test
    void testEdgeCases() {
        // Test edge cases: empty string, null, special characters
        assertFalse(profanityFilterService.hasProfanity(""));
        // Expect NullPointerException for null input
        assertThrows(NullPointerException.class, () -> profanityFilterService.hasProfanity(null));
        assertFalse(profanityFilterService.hasProfanity("!@#$%^&*()"));
        assertEquals("", profanityFilterService.filterProfanity(""));
        // Expect NullPointerException for null input
        assertThrows(NullPointerException.class, () -> profanityFilterService.filterProfanity(null));
        assertEquals("!@#$%^&*()", profanityFilterService.filterProfanity("!@#$%^&*()"));
    }
}