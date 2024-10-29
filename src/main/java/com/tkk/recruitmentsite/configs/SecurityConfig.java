package com.tkk.recruitmentsite.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    // Authentication provider for hndling user authentication
    private final AuthenticationProvider authenticationProvider;
    // JWT filter for processiong authentication tokens
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    // Constructor to inject dependencies
    public SecurityConfig(AuthenticationProvider authenticationProvider,
            JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.authenticationProvider = authenticationProvider;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    // Configures the security filtre chain
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disables CSRF protection
                .csrf(AbstractHttpConfigurer::disable)
                // Configures URL authroization
                .authorizeHttpRequests(auth -> auth
                        // Allows unauthenticated access to specified endpoints
                        .requestMatchers("/auth/**", "/**", "/signup", "/login").permitAll()
                        .requestMatchers("/styles.css").permitAll()
                        // Requires authentication for all other requsts
                        .anyRequest().authenticated())
                // Configures session management
                .sessionManagement(session -> session
                        // Sets session to stateless
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Sets the authetication provider
                .authenticationProvider(authenticationProvider)
                // Adds JWT filter before the username/password authentication filter
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        // Builds the security filter chain
        return http.build();
    }

    // Configures CORS settings
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Allows requests from localhost:8080
        configuration.setAllowedOrigins(List.of("http://localhost:8080"));
        // Allows GET and POST methods
        configuration.setAllowedMethods(List.of("GET", "POST"));
        // Allows specified headers
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        // Applies CORS configuration to all endpoints
        source.registerCorsConfiguration("/**", configuration);

        // Returns the CORS configuration source
        return source;
    }
}
