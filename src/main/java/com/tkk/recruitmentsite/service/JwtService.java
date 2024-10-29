package com.tkk.recruitmentsite.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jwts.SIG;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import javax.crypto.SecretKey;

@Service
public class JwtService {
    // Secret key for JWT encode, injected from application properties
    @Value("${security.jwt.secret-key}")
    private String secretKey;

    // JWT expiration time, injected from the application properties
    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;

    // Extracts the username from the given JWT token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extract a specific claim from the JWT token using a provided function
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // parses and verifies the Jwt token, then extracts all claims
    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // Generates a JWT token with default claims for th provided user details
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    // Generates a JWT token with custom claims and user details
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }

    // Returns the configred JWT expiration time
    public long getExpirationTime() {
        return jwtExpiration;
    }

    // Builds and signs a JWT Token with given claims, user details,
    // and expiration time.
    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration) {

        JwtBuilder builder = Jwts.builder()
                .claims(extraClaims)
                .signWith(getSignInKey(), SIG.HS256);

        // Adds the subject, issued at, and expiration claims
        // Subject
        builder.claim("sub", userDetails.getUsername());
        // Issued at
        builder.claim("iat", new Date(System.currentTimeMillis()));
        // Expiration
        builder.claim("exp", new Date(System.currentTimeMillis() + expiration));

        return builder.compact();
    }

    // Validates the JWT Token by checking its username and expiration
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    // Checks if the JWT token expired
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Extracts the expiration data from the JWT token
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Decodes the base64-encoded secret key and returns it as a SecretKey Object
    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
