package edu.uscb.csci470sp25.brighten_up_backend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uscb.csci470sp25.brighten_up_backend.repository.AppUserRepository;
import edu.uscb.csci470sp25.brighten_up_backend.security.JwtAuthenticationFilter;
import edu.uscb.csci470sp25.brighten_up_backend.security.JwtAuthEntryPoint;
import edu.uscb.csci470sp25.brighten_up_backend.security.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final JwtAuthEntryPoint jwtAuthEntryPoint;
    private final AppUserRepository appUserRepository;

    public SecurityConfig(JwtUtil jwtUtil, JwtAuthEntryPoint jwtAuthEntryPoint, AppUserRepository appUserRepository) {
        this.jwtUtil = jwtUtil;
        this.jwtAuthEntryPoint = jwtAuthEntryPoint;
        this.appUserRepository = appUserRepository;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                    // Public endpoints
                    .requestMatchers("/auth/register", "/auth/login").permitAll()
                    .requestMatchers(HttpMethod.GET, "/users").permitAll()
                    .requestMatchers(HttpMethod.GET, "/userposts").permitAll()
                    .requestMatchers(HttpMethod.GET, "/users/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/user/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/userposts/by-user/{userId}").permitAll()
                    .requestMatchers(HttpMethod.GET, "/userposts/{postId}/my-rating").permitAll()
                    .requestMatchers(HttpMethod.GET, "/userposts/{postId}/username").permitAll()
                    .requestMatchers(HttpMethod.POST, "/xai/chat/completions").permitAll() // Issue with accessing Grok with any restrictions
                    
                    // Authenticated users
                    .requestMatchers(HttpMethod.POST, "/userposts").authenticated()
                    .requestMatchers(HttpMethod.POST, "/userposts/{postId}/rate").authenticated()

                    // Admin-only endpoints
                    .requestMatchers(HttpMethod.POST, "/admin/register").hasAuthority("ADMIN") 
                    .requestMatchers(HttpMethod.DELETE, "/user/{userId}").hasAuthority("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/userposts/{postId}").hasAuthority("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/userposts/by-user/{userId}").hasAuthority("ADMIN")

                    // Everything else must be authenticated
                    .anyRequest().authenticated()
                )
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint(jwtAuthEntryPoint)
                .accessDeniedHandler(accessDeniedHandler()))
            .addFilterBefore(new JwtAuthenticationFilter(jwtUtil, appUserRepository), UsernamePasswordAuthenticationFilter.class)
            .httpBasic(httpBasic -> httpBasic.disable())
            .formLogin(form -> form.disable());

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // Allowed frontend origins
        config.setAllowedOrigins(List.of(
            "http://localhost:5173",
            "https://brightenup.netlify.app"
        ));
        
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (HttpServletRequest request, HttpServletResponse response, AccessDeniedException ex) -> {
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);

            Map<String, Object> error = new HashMap<>();
            error.put("status", 403);
            error.put("error", "Forbidden");
            error.put("message", "Access denied: Insufficient permissions");
            error.put("path", request.getRequestURI());

            new ObjectMapper().writeValue(response.getWriter(), error);
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}