package main.xiaoqihui.common.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Service
public class RedisTokenStore {

    private final StringRedisTemplate redisTemplate;
    private final String tokenPrefix;

    public RedisTokenStore(
        StringRedisTemplate redisTemplate,
        @Value("${app.auth.token-prefix:xqh:token}") String tokenPrefix
    ) {
        this.redisTemplate = redisTemplate;
        this.tokenPrefix = tokenPrefix;
    }

    public void saveTokens(String username, String accessToken, long accessExpireMs, String refreshToken, long refreshExpireMs) {
        redisTemplate.opsForValue().set(accessKey(username), accessToken, accessExpireMs, TimeUnit.MILLISECONDS);
        redisTemplate.opsForValue().set(refreshKey(username), refreshToken, refreshExpireMs, TimeUnit.MILLISECONDS);
    }

    public boolean validateAccessToken(String username, String token) {
        String cached = redisTemplate.opsForValue().get(accessKey(username));
        return token != null && token.equals(cached);
    }

    public boolean validateRefreshToken(String username, String token) {
        String cached = redisTemplate.opsForValue().get(refreshKey(username));
        return token != null && token.equals(cached);
    }

    public void revokeTokens(String username) {
        redisTemplate.delete(accessKey(username));
        redisTemplate.delete(refreshKey(username));
    }

    public void saveEmailCode(String emailKey, String code, Duration ttl) {
        redisTemplate.opsForValue().set(emailKey, code, ttl);
    }

    public String getEmailCode(String emailKey) {
        return redisTemplate.opsForValue().get(emailKey);
    }

    public void deleteEmailCode(String emailKey) {
        redisTemplate.delete(emailKey);
    }

    private String accessKey(String username) {
        return tokenPrefix + ":access:" + username;
    }

    private String refreshKey(String username) {
        return tokenPrefix + ":refresh:" + username;
    }
}
