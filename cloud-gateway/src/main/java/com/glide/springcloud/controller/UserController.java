package com.glide.springcloud.controller;


import com.glide.springcloud.model.CloudUser;
import com.glide.springcloud.service.LoginService;
import com.glide.springcloud.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;


@RestController
@RequestMapping("/api")
//@EnableMethodSecurity
public class UserController {

    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    LoginService authService;
    @Autowired
    RegisterService userService;

    //@Operation(summary = "登录以后返回token")
    @PostMapping(value = "/user/login")
    public Mono<Map<String, Object>> login(@RequestBody CloudUser cloudUser) {
        return authService.login(cloudUser);
    }

    @PostMapping(value = "/user/register")
    Map<String, Object> register(@RequestBody CloudUser cloudUser) {
        return userService.register(cloudUser);
    }

    @GetMapping(value = "/test")
    @PreAuthorize("hasAuthority('GUEST')")
    public Mono<String> test() {
        return Mono.just("test");
    }
}