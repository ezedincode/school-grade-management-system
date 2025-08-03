package com.ezedin.api_gateway.Service;


import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class redisService {
    private final RedisTemplate<String, String> redisTemplate;



    public boolean isAccessTokenValid(String jti) {
        return redisTemplate.hasKey("access:" + jti);
    }


    public void revokeAccessToken(String jti) {
        redisTemplate.delete("access:" + jti);
    }

}
