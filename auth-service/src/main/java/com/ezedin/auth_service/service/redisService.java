package com.ezedin.auth_service.service;


import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class redisService {
    private final RedisTemplate<String, String> redisTemplate;



    public void storeAccessToken(String jti, String userId, Long expirationSeconds) {
        String key = "access:" + jti;
        redisTemplate.opsForValue().set(key, userId, expirationSeconds, TimeUnit.SECONDS);
    }
    public void storeJti(String jti, String sessionID, Long expirationSeconds) {
        String key = "sessionIdForJti:" + sessionID;
        redisTemplate.opsForValue().set(key, jti, expirationSeconds, TimeUnit.SECONDS);
    }
    public String getJti(String sessionId) {
        String key = "sessionIdForJti:" + sessionId;
        return redisTemplate.opsForValue().get(key);
    }
    public void revokeJti(String sessionId) {
        String key = "sessionIdForJti:" + sessionId;
        redisTemplate.delete(key);
    }
    public void addSessionForUser(String userId, String sessionId) {
        String key = "user:" + userId + ":sessions";
        redisTemplate.opsForSet().add(key, sessionId);
    }
    public Set<String> getSessionsForUser(String userId) {
        String key = "user:" + userId + ":sessions";
        return redisTemplate.opsForSet().members(key);
    }
    public void removeSessionFromUser(String userId, String sessionId) {
        String key = "user:" + userId + ":sessions";
        redisTemplate.opsForSet().remove(key, sessionId);
    }
    public void deleteAllSessionsForUser(String userId) {
        String key = "user:" + userId + ":sessions";
        Set<String> sessions = redisTemplate.opsForSet().members(key);
        if (sessions != null) {
            for (String sessionId : sessions) {
                redisTemplate.delete("session:" + sessionId);
                redisTemplate.delete("refresh:" + sessionId);
                // Optional: delete "access:<jti>" if you store jti per session
            }
        }
        redisTemplate.delete(key); // delete the session set itself
    }


    public void storeRefreshToken(String sessionId, String refreshToken, Long expirationSeconds) {
        String key = "refresh:" + sessionId;
        redisTemplate.opsForValue().set(key, refreshToken, expirationSeconds, TimeUnit.SECONDS);
    }
    public void storeSessionId(String sessionId,String userId, Long expirationSeconds) {
        String key = "sessionId:" + sessionId;
        redisTemplate.opsForValue().set(key, userId, expirationSeconds, TimeUnit.SECONDS);
    }

    public boolean isAccessTokenValid(String jti) {
        return redisTemplate.hasKey("access:" + jti);
    }

    public boolean isRefreshTokenValid(String sessionId) {
        String stored = redisTemplate.opsForValue().get("refresh:"+sessionId);
        return stored != null;
    }
    public String getUserId(String sessionId) {
        return redisTemplate.opsForValue().get("sessionId:"+sessionId);
    }

    public void revokeAccessToken(String jti) {
        redisTemplate.delete("access:" + jti);
    }
    public String getRefreshToken(String sessionId) {
        return redisTemplate.opsForValue().get("refresh:"+sessionId);
    }

    public void revokeRefreshToken(String sessionId) {
        redisTemplate.delete("refresh:" + sessionId);
    }
    public void revokeSessionId(String sessionId) {
        redisTemplate.delete("sessionId:" + sessionId);
    }
}
