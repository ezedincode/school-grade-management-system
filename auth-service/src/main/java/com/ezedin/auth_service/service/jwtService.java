package com.ezedin.auth_service.service;

import com.ezedin.auth_service.model.User;
import io.jsonwebtoken.Claims;
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

    @Value("${Access_Token_Expiration_Time}")
    private Long ExpirationTime;

    @Value("${SECRET_KEY}")
    private String SECRET_KEY;
    public String generateToken(HashMap<String, Object> extraClaims, User user) {
        String jti = UUID.randomUUID().toString();
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(user.getUsername())
                .setId(jti)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + ExpirationTime))
                .signWith(getSignkey())
                .compact();

    }
    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder().withoutPadding();

    @Value("${REFRESH_TOKEN_BYTE_LENGTH}")
    private int byteLength;
    public String generateRefreshToken(String sessionId) {
       byte[] randomBytes = new byte[byteLength];
       secureRandom.nextBytes(randomBytes);
       return "sessionId:" + base64Encoder.encodeToString(randomBytes);
    }
    public String generateSessionId(){
        byte[] bytes = new byte[16];
        secureRandom.nextBytes(bytes);
        return base64Encoder.encodeToString(bytes);
    }
    public String extractJti(String token) {
        return extractAllClaims(token).getId(); // getId() returns the 'jti' claim
    }



    //    public <T> T extractClaim(String token, Function<Claims ,T> claimResolver) {
//        final Claims claims =extractAllClaims(token);
//        return claimResolver.apply(claims);
//    }
//
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignkey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
//    public String extractUsername(String token) {
//        return extractClaim(token, Claims::getSubject);
//    }

    private Key getSignkey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUserId( String Token) {
        return extractAllClaims(Token).getSubject();

    }
}

