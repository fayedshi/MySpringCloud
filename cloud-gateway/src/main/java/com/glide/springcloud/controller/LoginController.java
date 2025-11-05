package com.glide.springcloud.controller;


import com.glide.springcloud.util.JwtUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.bouncycastle.jcajce.BCFKSLoadStoreParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.concurrent.TimeUnit;

import io.jsonwebtoken.Claims;
//import io.;
//import io.jsonwebtoken.security.*;

@RestController
//@RequestMapping("/consumer")
public class LoginController {

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    JwtUtil jwtUtil;

    //    @Operation(summary = "登录以后返回token")
    @PostMapping(value = "/login")
    public String login(@RequestParam String userName, @RequestParam String password) {
//        String userToken = String.format("%s:%s", userName, UUID.randomUUID());
        String userToken = jwtUtil.generateToken(new HashMap<>(), userName);
        redisTemplate.opsForValue().set(userToken, userName, 2, TimeUnit.HOURS);
        return userToken;
    }


}