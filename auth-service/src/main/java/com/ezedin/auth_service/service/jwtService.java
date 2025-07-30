package com.ezedin.auth_service.service;

import com.ezedin.auth_service.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

@Service
public class jwtService {

    @Value("${ExpirationTime}")
    private int ExpirationTime;

    @Value("${SECRET_KEY}")
    private String SECRET_KEY;
    public String generateToken(HashMap<String, Object> extraClaims, User user) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + ExpirationTime))
                .signWith(getSignkey())
                .compact();

    }
    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder().withoutPadding();

    @Value("${REFRESH_TOKEN_BYTE_LENGTH}")
    private int byteLength;
    public String generateRefreshToken() {
       byte[] randomBytes = new byte[byteLength];
       secureRandom.nextBytes(randomBytes);
       return base64Encoder.encodeToString(randomBytes);
    }

//    public <T> T extractClaim(String token, Function<Claims ,T> claimResolver) {
//        final Claims claims =extractAllClaims(token);
//        return claimResolver.apply(claims);
//    }
//
//    private Claims extractAllClaims(String token) {
//        return Jwts
//                .parserBuilder()
//                .setSigningKey(getSignkey())
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//    }
//    public String extractUsername(String token) {
//        return extractClaim(token, Claims::getSubject);
//    }

    private Key getSignkey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

