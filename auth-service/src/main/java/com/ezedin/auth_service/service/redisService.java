package com.ezedin.auth_service.service;


import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class redisService {
    private final RedisTemplate<String, String> redisTemplate;



    public void storeAccessToken(String jti, String userId, Long expirationSeconds) {
        String key = "access:" + jti;
        redisTemplate.opsForValue().set(key, userId, expirationSeconds, TimeUnit.SECONDS);
    }


    public void storeRefreshToken(String userId, String refreshToken, Long expirationSeconds) {
        String key = "refresh:" + userId;
        redisTemplate.opsForValue().set(key, refreshToken, expirationSeconds, TimeUnit.SECONDS);
    }
    public void storeSessionId(String sessionId,String userId, Long expirationSeconds) {
        String key = "sessionId:" + sessionId;
        redisTemplate.opsForValue().set(key, userId, expirationSeconds, TimeUnit.SECONDS);
    }

    public boolean isAccessTokenValid(String jti) {
        return redisTemplate.hasKey("access:" + jti);
    }

    public boolean isRefreshTokenValid(String userId) {
        String stored = redisTemplate.opsForValue().get("refresh:"+userId);
        return stored != null;
    }
    public String getUserId(String sessionId) {
        return redisTemplate.opsForValue().get("sessionId:"+sessionId);
    }

    public void revokeAccessToken(String jti) {
        redisTemplate.delete("access:" + jti);
    }
    public String getRefreshToken(String userId) {
        return redisTemplate.opsForValue().get("refresh:"+userId);
    }

    public void revokeRefreshToken(String userId) {
        redisTemplate.delete("refresh:" + userId);
    }
    public void revokeSessionId(String sessionId) {
        redisTemplate.delete("sessionId:" + sessionId);
    }
}
