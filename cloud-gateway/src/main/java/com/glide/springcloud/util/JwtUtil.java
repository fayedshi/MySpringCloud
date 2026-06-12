package com.glide.springcloud.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import io.jsonwebtoken.Jwts;

import javax.crypto.spec.SecretKeySpec;

public class JwtUtil {

    /**
     * @param claims
     * @param subject
     * @return
     */
    public static String generateToken(Map<String, Object> claims, String subject, String secret) {
//        System.out.println(secret.getBytes(StandardCharsets.UTF_8).length);
        return Jwts.builder()
                .header().add("typ", "JWT").and()
                .claims(claims)
                .subject(subject) // user name
                .issuedAt(new Date(System.currentTimeMillis()))
                .id(UUID.randomUUID().toString())// 设置一个随机生成的唯一标识符。
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 48)) // 48小时
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .compact();
    }

    // invoked by CustomJwtFilter
    public static Claims validateToken(String token, String secret) {
        SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
