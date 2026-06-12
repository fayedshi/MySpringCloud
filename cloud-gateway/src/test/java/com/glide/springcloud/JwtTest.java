package com.glide.springcloud;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.glide.springcloud.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.impl.TextCodec;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import javax.crypto.Mac;

@SpringBootTest(classes = JwtTest.class)
public class JwtTest {

    @Value("${jwt.secret}")
    String secret;

    String decodeByBase64(String encodedStr) {
        byte[] bytes = TextCodec.BASE64.decode(encodedStr);
        return new String(bytes);
    }

    @Test
    void testTokenGen() throws JsonProcessingException {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", "Admin");
        claims.put("department", "IT");
        claims.put("username", "John");
        String token = JwtUtil.generateToken(claims, "allen", secret);
        System.out.println(token);
        String[] parts = token.split("\\.");
        String header = decodeByBase64(parts[0]);
        assertEquals(parts.length, 3);

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> headerMap = mapper.readValue(header, new TypeReference<>() {
        });
//        System.out.println(headerMap);
        assertEquals(headerMap.get("typ"), "JWT");
        String payload = decodeByBase64(parts[1]);
        Map<String, Object> payloadMap = mapper.readValue(payload, new TypeReference<>() {
        });
        assertEquals(payloadMap.get("username"), "John");
//        System.out.println(header);
//        System.out.println(playload);
    }

    @Test
    void testValidateToken() throws NoSuchAlgorithmException, InvalidKeyException {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", "Admin");
        claims.put("department", "IT");
        String token = JwtUtil.generateToken(claims, "User Info", secret);
        System.out.println(token);
        String[] parts = token.split("\\.");
        String header = decodeByBase64(parts[0]);
        String payload = decodeByBase64(parts[1]);

        System.out.println(header);
        System.out.println(payload);
        Claims claim = JwtUtil.validateToken(token, secret);
        assertEquals(claim.getSubject(), "User Info");
        assertEquals(claim.get("role"), "Admin");
    }

    @Test
    void testSigVerify() throws NoSuchAlgorithmException, InvalidKeyException {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", "Admin");
        claims.put("department", "IT");
        String token = JwtUtil.generateToken(claims, "User Info", secret);
        String[] parts = token.split("\\.");
        Key key = Keys.hmacShaKeyFor(secret.getBytes());
//        SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(key);
        byte[] bytes = mac.doFinal((parts[0] + "." + parts[1]).getBytes());
        String signature = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        assertEquals(signature, parts[2]);
    }

    @Test
    void testString() {
        System.out.printf("[dl.queue] %s Received message: , deliveryTag:", LocalDateTime.now());
    }
}
