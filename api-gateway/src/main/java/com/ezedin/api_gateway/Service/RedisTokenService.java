package com.ezedin.api_gateway.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RedisTokenService {
    private final RedisTemplate<String, String> redisTemplate;
    public boolean isTokenPresent(String jti) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(jti));
    }
}
