package com.ezedin.api_gateway.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
@Slf4j
@RequiredArgsConstructor
public class JwtUtil {
    @Value("${SECRET_KEY}")
    private String jwtSecret;
    private final redisService redis;

    public boolean isTokenValid(String token) {
        try {
            Claims claims = extractAllClaims(token);
            String jti=getJti(token);
            if(!redis.isAccessTokenValid(jti)) {
                return false;
            }
            if(isTokenExpired(claims)){
                redis.revokeAccessToken(token);
            }
            return !isTokenExpired(claims);
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

}
