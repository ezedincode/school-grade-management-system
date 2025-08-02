package com.ezedin.api_gateway.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
@Slf4j
public class JwtUtil {
    @Value("${SECRET_KEY}")
    private String jwtSecret;
    // In JwtUtil, add this debug:
    @PostConstruct
    public void logKey() {
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        log.info("JWT Key (Hex): {}", Hex.encodeHexString(keyBytes));
    }
    public boolean isTokenValid(String token) {
        try {
            Claims claims = extractAllClaims(token);
            return !isTokenExpired(claims);  // Check expiration
        } catch (JwtException | IllegalArgumentException e) {
            log.error("JWT validation failed: {}", e.getMessage());
            return false;
        }
    }

    private boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }

    public Claims extractAllClaims(String token) {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(keyBytes))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String getJti(String token) {
        return extractAllClaims(token).getId(); // jti field
    }
    private boolean isExpired (String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }
}
