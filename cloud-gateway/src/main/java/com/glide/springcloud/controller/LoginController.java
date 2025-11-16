package com.glide.springcloud.controller;


import com.glide.springcloud.model.CloudUser;
import com.glide.springcloud.service.AuthService;
import com.glide.springcloud.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.*;


@RestController
@RequestMapping("/api")
public class LoginController {

    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    AuthService authService;

    //@Operation(summary = "登录以后返回token")
    @PostMapping(value = "/user/login")
    public Mono<Object> login(@RequestBody CloudUser cloudUser) {
//        System.out.print("here");
//        String userToken = String.format("%s:%s", userName, UUID.randomUUID());
//        String userToken = jwtUtil.generateToken(new HashMap<>(), cloudUser.getUsername());
//        redisTemplate.opsForValue().set(userToken, cloudUser.getUsername(), 2, TimeUnit.HOURS);
        return authService.login(cloudUser);
    }

    @GetMapping(value = "/test")
    public Mono<String> test() {
        System.out.println("test");
        return Mono.just("test");
    }
}