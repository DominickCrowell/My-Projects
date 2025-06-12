package edu.uscb.csci470sp25.brighten_up_backend.security;

import edu.uscb.csci470sp25.brighten_up_backend.model.AppUser;
import edu.uscb.csci470sp25.brighten_up_backend.repository.AppUserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtUtil jwtUtil;
    private final AppUserRepository appUserRepository;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, AppUserRepository appUserRepository) {
        this.jwtUtil = jwtUtil;
        this.appUserRepository = appUserRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = extractToken(request);
        logger.info("Extracted token: {}", token != null ? token : "null");
        if (token != null) {
            try {
                Claims claims = jwtUtil.validateToken(token);
                logger.info("Token validated. Claims: {}", claims);
                Long userId = Long.parseLong(claims.getSubject());
                AppUser user = appUserRepository.findById(userId)
                        .orElseThrow(() -> new RuntimeException("User not found from token"));
                UserDetails userDetails = new CustomUserDetails(user);
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
                logger.info("Authentication set for userId: {}", userId);
            } catch (JwtException e) {
                if (e instanceof ExpiredJwtException) {
                    logger.error("JWT Token Expired: {}", e.getMessage(), e);
                } else {
                    logger.error("JWT Token Invalid: {}", e.getMessage(), e);
                }
                SecurityContextHolder.clearContext();
            } catch (Exception e) {
                logger.error("Unexpected error in JwtAuthenticationFilter: {}", e.getMessage(), e);
                SecurityContextHolder.clearContext();
            }
        } else {
            logger.warn("No token found in request");
        }
        filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        logger.info("Authorization header: {}", authHeader);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
}