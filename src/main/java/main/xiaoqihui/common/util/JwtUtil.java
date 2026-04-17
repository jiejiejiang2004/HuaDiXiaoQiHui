package main.xiaoqihui.common.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import main.xiaoqihui.common.security.LoginUser;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    private final String accessSecretKey;
    private final long accessExpirationTime;
    private final String refreshSecretKey;
    private final long refreshExpirationTime;

    public JwtUtil(Environment environment) {
        this.accessSecretKey = environment.getProperty("jwt.access.secret");
        this.accessExpirationTime = Long.parseLong(environment.getProperty("jwt.access.expiration", "7200000"));
        this.refreshSecretKey = environment.getProperty("jwt.refresh.secret");
        this.refreshExpirationTime = Long.parseLong(environment.getProperty("jwt.refresh.expiration", "604800000"));
    }

    public String generateAccessToken(LoginUser loginUser) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", loginUser.getUserId());
        claims.put("userType", loginUser.getUserType());
        claims.put("realName", loginUser.getRealName());
        return createToken(claims, loginUser.getUsername(), accessSecretKey, accessExpirationTime);
    }

    public String generateRefreshToken(LoginUser loginUser) {
        return createToken(new HashMap<>(), loginUser.getUsername(), refreshSecretKey, refreshExpirationTime);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean validateAccessToken(String token) {
        return validateToken(token, accessSecretKey);
    }

    public boolean validateRefreshToken(String token) {
        return validateToken(token, refreshSecretKey);
    }

    public long getAccessExpirationTime() {
        return accessExpirationTime;
    }

    private String createToken(Map<String, Object> claims, String subject, String secretKey, long expirationTime) {
        return Jwts.builder()
            .claims(claims)
            .subject(subject)
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + expirationTime))
            .signWith(getSignInKey(secretKey))
            .compact();
    }

    private boolean validateToken(String token, String secretKey) {
        try {
            return !extractClaim(token, Claims::getExpiration, secretKey).before(new Date());
        } catch (Exception ex) {
            return false;
        }
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        return extractClaim(token, claimsResolver, accessSecretKey);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver, String secretKey) {
        Claims claims = extractAllClaims(token, secretKey);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token, String secretKey) {
        try {
            return Jwts.parser()
                .verifyWith(getSignInKey(secretKey))
                .build()
                .parseSignedClaims(token)
                .getPayload();
        } catch (ExpiredJwtException ex) {
            return ex.getClaims();
        }
    }

    private SecretKey getSignInKey(String secretKey) {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }
}
