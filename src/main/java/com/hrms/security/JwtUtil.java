package com.hrms.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.util.function.Function;

@Component
//job of this class to create token alone
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

     public String generateToken(String username) {
        //Key(String) -> Bytes -> Key Object by UTf-8 encoding
        SecretKey key = getSigningKey();

        return Jwts.builder()
                .subject(username)//sub
                .issuedAt(new Date())//iat
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(key)
                .compact();//combines into final JWT String
    }

    
    /**
     * Extract Username (sub claim)
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

     /**
     * Generic Claim Extractor
     */
    public <T> T extractClaim(String token,
                              Function<Claims, T> claimsResolver) {

        Claims claims = extractAllClaims(token);

        return claimsResolver.apply(claims);
    }

    /**
     * Read all Claims from JWT
     */
    private Claims extractAllClaims(String token) {

        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

    }

    /**
     * Check Token Expiry
     */
    public boolean isTokenExpired(String token) {
        
        //Claims::getExpiration - This is a method reference, a shorthand way in Java to call an existing method.
        Date expiration = extractClaim(token, Claims::getExpiration);

        return expiration.before(new Date());

    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Validate JWT
     */
    public boolean validateToken(String token,
                                 UserDetails userDetails) {

        String username = extractUsername(token);

        return username.equals(userDetails.getUsername())
                && !isTokenExpired(token);

    }

}