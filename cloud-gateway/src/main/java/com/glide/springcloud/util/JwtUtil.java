package com.glide.springcloud.util;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.core.annotation.Order;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;

public class JwtUtil {

    //    @Value("${jwt.secret}")
    private static final String secret = "turnMeOnturnMeOnturnMeOnturnMeOnturnMeOnturnMeOn";

    /**
     * @param claims
     * @param subject
     * @return
     */
    public static String generateToken(Map<String, Object> claims, String subject) {

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .claims(claims)
                .subject(subject) // user name
                .issuedAt(new Date(System.currentTimeMillis()))
                .id(UUID.randomUUID().toString())// 设置一个随机生成的唯一标识符。
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 48)) // 10小时
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    // invoked by gateway filter
    public static Claims validateToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .build().
                parseClaimsJws(token)
                .getBody();
    }
}
