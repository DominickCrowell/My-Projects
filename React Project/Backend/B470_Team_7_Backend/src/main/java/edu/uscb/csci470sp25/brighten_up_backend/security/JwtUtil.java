package edu.uscb.csci470sp25.brighten_up_backend.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct; // Correct import for @PostConstruct
import java.util.Date;

@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    @Value("${jwt.secret}")
    private String secretKey;

    private static final long EXPIRATION_TIME = 86400000; // 24 hours

    @PostConstruct
    public void init() {
        logger.info("JwtUtil initialized with secretKey: {}", secretKey != null ? secretKey : "null");
    }

    private SecretKey getSigningKey() {
        try {
            byte[] keyBytes = Decoders.BASE64.decode(secretKey);
            logger.info("Decoded secret key length: {}", keyBytes.length);
            return Keys.hmacShaKeyFor(keyBytes);
        } catch (Exception e) {
            logger.error("Failed to create signing key: {}", e.getMessage(), e);
            throw e;
        }
    }

    public String generateToken(String email, String role, Long userId) {
        try {
            String token = Jwts.builder()
                    .subject(userId.toString())
                    .claim("email", email)
                    .claim("role", role)
                    .issuedAt(new Date())
                    .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                    .signWith(getSigningKey())
                    .compact();
            logger.info("Generated token for userId {}: {}", userId, token);
            return token;
        } catch (Exception e) {
            logger.error("Failed to generate token: {}", e.getMessage(), e);
            throw e;
        }
    }

    public Claims validateToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            logger.info("Validated token: {}", token);
            return claims;
        } catch (Exception e) {
            logger.error("Token validation failed for token {}: {}", token, e.getMessage(), e);
            throw e;
        }
    }

    public Long extractUserId(String token) {
        Claims claims = validateToken(token);
        return Long.valueOf(claims.getSubject());
    }

    public String extractEmail(String token) {
        return validateToken(token).get("email", String.class);
    }

    public String extractRole(String token) {
        return validateToken(token).get("role", String.class);
    }
}